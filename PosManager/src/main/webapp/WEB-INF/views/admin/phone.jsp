<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" pageEncoding="utf-8" %>
<c:set var="basePathEmpty" value="${ empty basePath  ? '' : '/' }"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta name="description" content="">
  <meta name="author" content="">
  <link rel="shortcut icon" href="./_static/assets/images/favicon.png" type="image/png">
  <title>ORDER9 Backoffice</title>
  <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600" rel="stylesheet" type="text/css">
   
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/css/bootstrap.min.css" integrity="sha384-Vkoo8x4CGsO3+Hhxv8T/Q5PaXtkKtu6ug5TOeNV6gBiFeWPGFN9MuhOf23Q9Ifjh" crossorigin="anonymous">
  
  <link href="https://developers.google.com/maps/documentation/javascript/examples/default.css" rel="stylesheet">
  
  
</head>
<body>


<div id="full-preloader">
  <div id="full-status"><i class="fa fa-spinner fa-spin"></i></div>
</div>
<section>

  <!-- LEFT PANEL -->
  <div id="leftPanel" class="leftpanel">
    <!-- async load... -->
  </div>

  <div class="mainpanel">
    <!-- HEADER BAR -->
    <div id="header" class="headerbar">
      <!-- async load ... -->
    </div>

    <!-- PAGE HEADER -->
    <div id="pageHeader" class="pageheader">
      <!-- async load ... -->
    </div>

    <!-- CONTENT PANEL -->
    <div id="main-wrapper" class="contentpanel">
      <!-- async load ... -->
    </div>

  </div><!-- mainpanel -->

  <!-- RIGHT PANEL -->
  <div id="rightPanel" class="rightpanel">
    <!-- async async ... -->
  </div>


</section>

<!-- Javascripts -->

<script src="https://code.jquery.com/jquery-3.4.1.slim.min.js" integrity="sha384-J6qa4849blE2+poT4WnyKhv5vZF5SrPo0iEjwBvKU7imGFAV0wwj1yYfoRSJoZ+n" crossorigin="anonymous"></script>
<script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.0/dist/umd/popper.min.js" integrity="sha384-Q6E9RHvbIyZFJoft+2mJbHaEWldlvI9IOYy5n3zV9zzTtmI3UksdQRVvoxMfooAo" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" integrity="sha384-wfSDF2E50Y2D1uUdj0O3uMBJnjuUD4Ih7YwaYd1iqfktj0Uod8GCExl3Og8ifwB6" crossorigin="anonymous"></script>

<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<script src="${basePath}${basePathEmpty}_static/js/config.js"></script>
<script data-main="${basePath}${basePathEmpty}_static/js/${dataMain}" src="${basePath}${basePathEmpty}_static/assets/plugins/require.js"></script>
<!-- END JAVASCRIPTS -->
</body>
</html>