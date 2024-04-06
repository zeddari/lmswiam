package com.wiam.lms.service.reporting;

import io.woo.htmltopdf.HtmlToPdf;
import io.woo.htmltopdf.HtmlToPdfObject;

public class PdfTest {

    public static void main(String arg[]) {
        boolean success = HtmlToPdf
            .create()
            .object(
                HtmlToPdfObject.forHtml(
                    "<html lang=\"ar\" dir=\"rtl\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                    "  <head>\n" +
                    "    <meta charset=\"UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                    "    <title>Document</title>\n" +
                    "    <link\n" +
                    "      href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css\"\n" +
                    "      rel=\"stylesheet\"\n" +
                    "      integrity=\"sha384-EVSTQN3/azprG1Anm3QDgpJLIm9Nao0Yz1ztcQTwFspd3yD65VohhpuuCOmLASjC\"\n" +
                    "      crossorigin=\"anonymous\"\n" +
                    "    />\n" +
                    "    <link\n" +
                    "      href=\"https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,500;0,600;0,700;1,300;1,400;1,500;1,600;1,700&amp;family=Tajawal:wght@200;300;400;500;700;800;900&amp;display=swap\"\n" +
                    "      rel=\"stylesheet\"\n" +
                    "    />\n" +
                    "\n" +
                    "    <style>\n" +
                    "  @font-face {\n" +
                    "     font-family: 'Arial';\n" +
                    "     font-display: Arial;\n" +
                    "     src: url('./fonts/Arial.ttf') format(\"ttf\")\n" +
                    "  }\n" +
                    "\n" +
                    "  html {\n" +
                    "     font-family: Arial;\n" +
                    "     --dark-brown: rgb(76, 29, 12);\n" +
                    "  }\n" +
                    "</style>\n" +
                    "  </head>\n" +
                    "  <body style=\"font-family: 'Tajawal', sans-serif; font-weight: 500\" class=\"m-5\">\n" +
                    "    <table class=\"table text-center table-borderless\">\n" +
                    "      <tr>\n" +
                    "        <td><img src=\"img/alwiam-logo.png\" alt=\"\" /></td>\n" +
                    "        <td style=\"font-size: 30px; text-align: center; font-weight: 600\">تقرير حلقة دوري</td>\n" +
                    "        <td><img src=\"img/alwiam-logo.png\" alt=\"\" /></td>\n" +
                    "      </tr>\n" +
                    "    </table>\n" +
                    "    <table class=\"table text-center\" style=\"font-size: 16px\">\n" +
                    "      <tr>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">إسم المدرسة</td>\n" +
                    "        <td style=\"width: 35%; border: 1px solid #191c17\"><span th:text=\"${pdfDetails[0].schoolName}\"></span></td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">إسم الحلقة</td>\n" +
                    "        <td style=\"width: 35%; border: 1px solid #191c17\">أبو بكر الصديق</td>\n" +
                    "      </tr>\n" +
                    "      <tr>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">إسم المعلم</td>\n" +
                    "        <td style=\"width: 35%; border: 1px solid #191c17\"></td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">الفئة</td>\n" +
                    "        <td style=\"width: 35%; border: 1px solid #191c17\">ذكر</td>\n" +
                    "      </tr>\n" +
                    "      <tr style=\"background-color: #9ba003; color: #fff\">\n" +
                    "        <td class=\"text-center align-middle\" rowspan=\"2\" style=\"width: 15%; border: 1px solid #191c17\">فترة التقرير</td>\n" +
                    "        <td class=\"text-center\" style=\"width: 35%; border: 1px solid #191c17\">هجري</td>\n" +
                    "        <td colspan=\"2\" class=\"text-center\" style=\"width: 50%; border: 1px solid #191c17\">من 20 شعبان 1445 إلى 21 رمضان 1445</td>\n" +
                    "      </tr>\n" +
                    "      <tr style=\"background-color: #9ba003; color: #fff\">\n" +
                    "        <td class=\"text-center\" style=\"width: 35%; border: 1px solid #191c17\">هجري</td>\n" +
                    "        <td colspan=\"2\" class=\"text-center\" style=\"width: 50%; border: 1px solid #191c17\">من 20 شعبان 1445 إلى 21 رمضان 1445</td>\n" +
                    "      </tr>\n" +
                    "    </table>\n" +
                    "    <table class=\"table\">\n" +
                    "      <tr>\n" +
                    "        <td colspan=\"8\" class=\"text-center\" style=\"border: 1px solid #191c17\">التقرير الإجمالي</td>\n" +
                    "      </tr>\n" +
                    "      <tr>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">عدد الطلاب</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">11</td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">عدد أيام الدوام</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">22</td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">دروس المنهج المصاحب</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">0</td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">عدد دروس التجويد</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">0</td>\n" +
                    "      </tr>\n" +
                    "      <tr>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">عدد الأجزاء</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">11</td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">عدد اجزاء مراجعة</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">22</td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">عدد أجزاء الواجب المنزلي</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">0</td>\n" +
                    "        <td style=\"width: 15%; background-color: #5344c6; color: #fff; border: 1px solid #191c17\">صفحات التلقين الجماعي</td>\n" +
                    "        <td style=\"width: 10%; color: #191c17; border: 1px solid #191c17\">0</td>\n" +
                    "      </tr>\n" +
                    "    </table>\n" +
                    "\n" +
                    "    <script\n" +
                    "      src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js\"\n" +
                    "      integrity=\"sha384-MrcW6ZMFYlzcLA8Nl+NtUVF0sA7MsXsP1UyJoMp4YLEuNSfAP+JcXn/tWtIaxVXM\"\n" +
                    "      crossorigin=\"anonymous\"\n" +
                    "    ></script>\n" +
                    "  </body>\n" +
                    "</html>\n"
                )
            )
            .convert("report.pdf");
    }
}
