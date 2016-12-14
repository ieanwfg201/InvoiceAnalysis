using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

using com.constantsoft.invoice;

namespace WindowsFormsApplication1
{
    public partial class Form1 : Form
    {
        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            OpenFileDialog ofd = new OpenFileDialog();
            if (pdfSelect.Checked)
                ofd.Filter = "所有pdf文件(*.pdf)|*.pdf";//设置文件类型
            else if (imageSelect.Checked)
                ofd.Filter = "所有图像文件(*.jpg;*.gif;*.bmp;*.png)|*.jpg;*.gif;*.bmp;*.png";//设置文件类型
            else
            {
                ofd.Filter = "所有pdf以及图像文件(*.pdf;*.jpg;*.gif;*.bmp;*.png)|*.pdf;*.jpg;*.gif;*.bmp;*.png";//设置文件类型
            }
            ofd.Title = "选择指定文件";

            string FileName = "";
            ofd.AddExtension = true;//是否自动增加所辍名
            ofd.AutoUpgradeEnabled = true;//是否随系统升级而升级外观
            if (ofd.ShowDialog() == DialogResult.OK)//如果点的是确定就得到文件路径
            {
                FileName = ofd.FileName;//得到文件路径
                errorLabel.Text = "";
                codeBox.Text = "-";
                numberBox.Text = "-";
                Boolean pdfOrImage = true; // pdf
                if (autoSelect.Checked)
                {
                    if (!FileName.ToLower().EndsWith(".pdf"))
                        pdfOrImage = false;
                }
                if (imageSelect.Checked)
                    pdfOrImage = false;

                fileNameBox.Text = FileName;
                try
                {
                    Dictionary<string, string> props = new Dictionary<string, string>();
                    props.Add("file.encoding", "UTF-8");
                    ikvm.runtime.Startup.setProperties(props);
                    //String[] arrays = InvoiceGenerator.instance().generateInvoiceCodeAndNumberValidateByPdfAndQrcode(FileName);
                    String[] arrays = null;
                    long startTime = DateTime.Now.Ticks / 10000;
                    if (pdfOrImage)
                    {
                        arrays = InvoiceGenerator.instance().generateInvoiceCodeAndNumber(FileName, true, false, false);
                    }
                    else
                    {
                        arrays = InvoiceGenerator.instance().generateInvoiceCodeAndNumber(FileName, false, true, false);
                    }
                    long endTime = DateTime.Now.Ticks / 10000;

                    costTimeBox.Text = (endTime - startTime) + "ms";

                    if (arrays == null || arrays.Length != 2 || (arrays[0] == "-"&&arrays[1] == "-"))
                    {
                        errorLabel.Text = "解析文件失败,无法从文件中获取到代码和编号";
                    }
                    else
                    {
                        codeBox.Text = arrays[0];
                        numberBox.Text = arrays[1];
                    }
                }
                catch (Exception ex)
                {
                    errorLabel.Text = "解析文件异常, 无法识别该文件: "+ex.Message;
                }
                finally
                {

                }

            }
        }

    }
}

