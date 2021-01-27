<!doctype html>
<html lang="en">
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shortcut icon" href="./_static/assets/images/favicon.png" type="image/png">

  <title>ORDER9 Backoffice</title>

  <link href="./_static/assets/css/style.default.css" rel="stylesheet">
  <link href="./_static/assets/css/custom.css" rel="stylesheet">
  <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
  <!--[if lt IE 9]>
  <script src="./_static/assets/js/html5shiv.js"></script>
  <script src="./_static/assets/js/respond.min.js"></script>
  <![endif]-->
</head>

<body class="signin">


<section>

    <div class="signinpanel">

        <div class="row">

            <div class="col-md-2"></div>

            <div class="col-md-8">

				<form action="./login/auth" method="post">
				
                    <h2 class="nomargin">Order9 BackofficeTest오더나인</h2>
                    <p class="mt5 mb20">Login to access your account.</p>

                    <input type="text" class="form-control uname" placeholder="User Id" name="username"/>
                    <input type="password" class="form-control pword" placeholder="Password" name="password"/>
                    <!-- <a href=""><small>Forgot Your Password?</small></a> -->
                    <button type="submit" class="btn btn-success btn-block" style='height:40px;'>LOGIN</button>

                </form>
            </div>
             <div class="col-md-2"></div>

        </div><!-- row -->

        <div class="signup-footer">
            <div class="div-center">
                 &copy; Order9, All Rights Reserved.
            </div>
        </div>

    </div><!-- signin -->

</section>


<script src="./_static/assets/js/jquery-1.11.1.min.js"></script>
<script src="./_static/assets/js/jquery-migrate-1.2.1.min.js"></script>
<script src="./_static/assets/js/bootstrap.min.js"></script>
<script src="./_static/assets/js/modernizr.min.js"></script>
<script src="./_static/assets/js/jquery.sparkline.min.js"></script>
<script src="./_static/assets/js/jquery.cookies.js"></script>
<script src="./_static/assets/js/toggles.min.js"></script>
<script src="./_static/assets/js/retina.min.js"></script>

<script src="./_static/assets/js/custom.js"></script>
<script>
    jQuery(document).ready(function(){

        // Please do not use the code below
        // This is for demo purposes only
        var c = jQuery.cookie('change-skin');
        if (c && c == 'greyjoy') {
            jQuery('.btn-success').addClass('btn-orange').removeClass('btn-success');
        } else if(c && c == 'dodgerblue') {
            jQuery('.btn-success').addClass('btn-primary').removeClass('btn-success');
        } else if (c && c == 'katniss') {
            jQuery('.btn-success').addClass('btn-primary').removeClass('btn-success');
        }
         
    });
</script>

</body>
</html>
