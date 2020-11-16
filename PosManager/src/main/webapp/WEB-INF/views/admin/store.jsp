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
  <link href="./_static/assets/css/style.default.css" rel="stylesheet">
  <link href="./_static/assets/css/prettyPhoto.css" rel="stylesheet">
  <link href="./_static/assets/plugins/jquery-ui/jquery-ui.css" rel="stylesheet">
  <link href="./_static/assets/plugins/datatables/css/jquery.dataTables.min.css" rel="stylesheet"/>
  <link href="./_static/assets/plugins/datatables/css/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>
  <link href="./_static/assets/plugins/select2/css/select2.min.css" rel="stylesheet" type="text/css"/>
  <link href="./_static/assets/css/font-awesome.min.css" rel="stylesheet">
  <link href="${basePath}${basePathEmpty}_static/assets/plugins/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet" type="text/css"/>
  <link href="http://cdn.datatables.net/fixedcolumns/3.0.2/css/dataTables.fixedColumns.min.css" rel="stylesheet">
  <link href="./_static/assets/css/bootstrap-timepicker.min.css" rel="stylesheet">
  <link href="./_static/assets/plugins/bootstrap-datepicker/css/datepicker3.css" rel="stylesheet">
  <link href="./_static/assets/css/custom.css" rel="stylesheet">
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
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<script src="${basePath}${basePathEmpty}_static/js/config.js"></script>
<script data-main="${basePath}${basePathEmpty}_static/js/${dataMain}" src="${basePath}${basePathEmpty}_static/assets/plugins/require.js"></script>
<!-- END JAVASCRIPTS -->
</body>
</html>