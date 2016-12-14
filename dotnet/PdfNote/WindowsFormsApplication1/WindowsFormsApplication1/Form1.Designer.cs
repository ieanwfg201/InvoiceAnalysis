namespace WindowsFormsApplication1
{
    partial class Form1
    {
        /// <summary>
        /// 必需的设计器变量。
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// 清理所有正在使用的资源。
        /// </summary>
        /// <param name="disposing">如果应释放托管资源，为 true；否则为 false。</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows 窗体设计器生成的代码

        /// <summary>
        /// 设计器支持所需的方法 - 不要修改
        /// 使用代码编辑器修改此方法的内容。
        /// </summary>
        private void InitializeComponent()
        {
            this.button1 = new System.Windows.Forms.Button();
            this.fileNameBox = new System.Windows.Forms.TextBox();
            this.codeBox = new System.Windows.Forms.TextBox();
            this.numberBox = new System.Windows.Forms.TextBox();
            this.label1 = new System.Windows.Forms.Label();
            this.label2 = new System.Windows.Forms.Label();
            this.errorLabel = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.autoSelect = new System.Windows.Forms.RadioButton();
            this.pdfSelect = new System.Windows.Forms.RadioButton();
            this.imageSelect = new System.Windows.Forms.RadioButton();
            this.label4 = new System.Windows.Forms.Label();
            this.costTimeBox = new System.Windows.Forms.TextBox();
            this.SuspendLayout();
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(69, 44);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 0;
            this.button1.Text = "选择文件";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // fileNameBox
            // 
            this.fileNameBox.Location = new System.Drawing.Point(160, 44);
            this.fileNameBox.Name = "fileNameBox";
            this.fileNameBox.ReadOnly = true;
            this.fileNameBox.Size = new System.Drawing.Size(343, 21);
            this.fileNameBox.TabIndex = 14;
            // 
            // codeBox
            // 
            this.codeBox.Location = new System.Drawing.Point(160, 168);
            this.codeBox.Name = "codeBox";
            this.codeBox.ReadOnly = true;
            this.codeBox.Size = new System.Drawing.Size(343, 21);
            this.codeBox.TabIndex = 2;
            // 
            // numberBox
            // 
            this.numberBox.Location = new System.Drawing.Point(160, 207);
            this.numberBox.Name = "numberBox";
            this.numberBox.ReadOnly = true;
            this.numberBox.Size = new System.Drawing.Size(343, 21);
            this.numberBox.TabIndex = 3;
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(90, 177);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(29, 12);
            this.label1.TabIndex = 4;
            this.label1.Text = "code";
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(92, 216);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(41, 12);
            this.label2.TabIndex = 5;
            this.label2.Text = "number";
            // 
            // errorLabel
            // 
            this.errorLabel.AutoSize = true;
            this.errorLabel.Location = new System.Drawing.Point(160, 68);
            this.errorLabel.Name = "errorLabel";
            this.errorLabel.Size = new System.Drawing.Size(0, 12);
            this.errorLabel.TabIndex = 6;
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(79, 9);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(65, 12);
            this.label3.TabIndex = 8;
            this.label3.Text = "File type:";
            // 
            // autoSelect
            // 
            this.autoSelect.AutoSize = true;
            this.autoSelect.Checked = true;
            this.autoSelect.FlatStyle = System.Windows.Forms.FlatStyle.System;
            this.autoSelect.Location = new System.Drawing.Point(162, 9);
            this.autoSelect.Name = "autoSelect";
            this.autoSelect.Size = new System.Drawing.Size(53, 17);
            this.autoSelect.TabIndex = 11;
            this.autoSelect.TabStop = true;
            this.autoSelect.Text = "Auto";
            this.autoSelect.UseVisualStyleBackColor = true;
            // 
            // pdfSelect
            // 
            this.pdfSelect.AutoSize = true;
            this.pdfSelect.Location = new System.Drawing.Point(254, 9);
            this.pdfSelect.Name = "pdfSelect";
            this.pdfSelect.Size = new System.Drawing.Size(41, 16);
            this.pdfSelect.TabIndex = 12;
            this.pdfSelect.TabStop = true;
            this.pdfSelect.Text = "PDF";
            this.pdfSelect.UseVisualStyleBackColor = true;
            // 
            // imageSelect
            // 
            this.imageSelect.AutoSize = true;
            this.imageSelect.Location = new System.Drawing.Point(360, 9);
            this.imageSelect.Name = "imageSelect";
            this.imageSelect.Size = new System.Drawing.Size(53, 16);
            this.imageSelect.TabIndex = 13;
            this.imageSelect.TabStop = true;
            this.imageSelect.Text = "Image";
            this.imageSelect.UseVisualStyleBackColor = true;
            // 
            // label4
            // 
            this.label4.AutoSize = true;
            this.label4.Location = new System.Drawing.Point(79, 138);
            this.label4.Name = "label4";
            this.label4.Size = new System.Drawing.Size(65, 12);
            this.label4.TabIndex = 15;
            this.label4.Text = " Cost Time";
            // 
            // costTimeBox
            // 
            this.costTimeBox.Location = new System.Drawing.Point(160, 129);
            this.costTimeBox.Name = "costTimeBox";
            this.costTimeBox.ReadOnly = true;
            this.costTimeBox.Size = new System.Drawing.Size(100, 21);
            this.costTimeBox.TabIndex = 16;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 12F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(573, 266);
            this.Controls.Add(this.costTimeBox);
            this.Controls.Add(this.label4);
            this.Controls.Add(this.imageSelect);
            this.Controls.Add(this.pdfSelect);
            this.Controls.Add(this.autoSelect);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.errorLabel);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.numberBox);
            this.Controls.Add(this.codeBox);
            this.Controls.Add(this.fileNameBox);
            this.Controls.Add(this.button1);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.TextBox fileNameBox;
        private System.Windows.Forms.TextBox codeBox;
        private System.Windows.Forms.TextBox numberBox;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label errorLabel;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.RadioButton autoSelect;
        private System.Windows.Forms.RadioButton pdfSelect;
        private System.Windows.Forms.RadioButton imageSelect;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TextBox costTimeBox;
    }
}

