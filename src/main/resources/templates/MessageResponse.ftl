<!DOCTYPE html>
<html lang="en">
<head>
    <title></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://fonts.googleapis.com/css?family=Poppins:400,500,600,700" rel="stylesheet" type="text/css">
    <style type="text/css">
        .root {
            overflow: hidden;
        }

        .header {
            color: #ffffff;
            text-transform: uppercase;
            background-color: #DC7633;
        }

        .container {
            overflow: hidden;
            padding: 15px 10px;
        }

        .row {
            margin-right: 15px;
            margin-left: 15px;
        }

        .dark-stroke {
            border-bottom: 2px solid #F8F9F9;
            width: 80px;
        }

        .content {
            background-color: #ECF0F1;
        }

        .content-td {
            font-size: 0;
            padding: 10px 25px;
            word-break: break-word;
        }

        .content-td > div {
            font-family: Poppins, Arial, sans-serif;
            font-size: 14px;
            line-height: 1.5;
            text-align: left;
            color: #000000;
        }

        .content-td-key {
            font-family: Poppins, Arial, sans-serif;
            font-size: 30px !important;
            font-weight: 600;
            line-height: 2;
            text-align: center !important;
            color: #DC7633 !important;
        }

        .footer {
            color: #ffffff;
            background-color: #DC7633;
        }

        .footer-td {
            font-size: 0;
            padding: 10px 25px;
            word-break: break-word;
        }

        .footer-td > div {
            font-family: Poppins, Helvetica, Arial, sans-serif;
            font-size: 13px;
            line-height: 1;
            text-align: left;
            color: #ffffff;
        }

        .footer-td > div > span {
            color: #ffffff;
        }

        .footer-social > a > img {
            height: 35px;
        }
    </style>
</head>
<body>
<div class="root">
    <section class="container header">
        <div class="row">
            <div class="col-md-12 features-title">
                <h2>Response to Message</h2>
                <div class="dark-stroke"></div>
            </div>
        </div>
    </section>
    <section class="container content">
        <table role="presentation">
            <tr>
                <td class="content-td">
                    <div>Hi ${name},</div>
                </td>
            </tr>
            <tr>
                <td class="content-td">
                    <div>
                        Thank you so much for reaching out! We received your message and this reply is to let you know
                        about your inquiry.
                    </div>
                </td>
            </tr>
            <tr>
                <td class="content-td">
                    <div>${reply}</div>
                </td>
            </tr>
            <tr>
                <td class="content-td">
                    <div class="content-td-key">
                        In spite of the upgrade, you can still visit our website and check out the previously asked
                        questions.
                        This will give you some clues about the inquiries you want to make.
                    </div>
                </td>
            </tr>
            <tr>
                <td class="content-td">
                    <div>Sincerely,<br/>Your friendly TickShow</div>
                </td>
            </tr>
        </table>
    </section>
    <section class="container footer">
        <table role="presentation">
            <tr>
                <td class="footer-td">
                    <div>Copyright © 2021/22 All rights reserved | | designed by <span>Pragashraj</span></div>
                </td>
            </tr>
        </table>
    </section>
</div>
</body>
</html>