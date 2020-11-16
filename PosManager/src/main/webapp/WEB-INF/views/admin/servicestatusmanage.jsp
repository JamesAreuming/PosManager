<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" pageEncoding="utf-8" %>
<c:set var="basePathEmpty" value="${ empty basePath  ? '' : '/' }"/>
<!DOCTYPE html>
<html>
<head>
  <!-- Title -->
  <title>ORDER9 Backoffice</title>

  <meta content="width=device-width, initial-scale=1" name="viewport"/>
  <meta charset="UTF-8">
  <meta name="description" content="Admin Dashboard Template" />
  <meta name="keywords" content="admin,dashboard" />
  <meta name="author" content="Steelcoders" />

  <!-- Styles -->
  <link href="http://fonts.googleapis.com/css?family=Open+Sans:400,300,600" rel="stylesheet" type="text/css">
  <link href="./_static/assets/css/style.default.css" rel="stylesheet">
  <link href="./_static/assets/plugins/jquery-ui/jquery-ui.css" rel="stylesheet">
  <link href="./_static/assets/plugins/datatables/css/jquery.dataTables.min.css" rel="stylesheet"/>
  <link href="./_static/assets/plugins/datatables/css/jquery.dataTables_themeroller.css" rel="stylesheet" type="text/css"/>
  <link href="./_static/assets/css/select2.css" rel="stylesheet" type="text/css"/>
  <link href="./_static/assets/css/font-awesome.min.css" rel="stylesheet">
  <link href="${basePath}${basePathEmpty}_static/assets/plugins/bootstrap-daterangepicker/daterangepicker.css" rel="stylesheet" type="text/css"/>
  <link href="http://cdn.datatables.net/fixedcolumns/3.0.2/css/dataTables.fixedColumns.min.css" rel="stylesheet">
  <link href="./_static/assets/css/bootstrap-timepicker.min.css" rel="stylesheet">
  <link href="./_static/assets/plugins/bootstrap-datepicker/css/datepicker3.css" rel="stylesheet">
  <link href="./_static/assets/css/custom.css" rel="stylesheet">
</head>
<body class="page-header-fixed page-horizontal-bar">

<main class="page-content content-wrap">
  <div id="header" class="navbar">
    <!-- header : Navbar -->
  </div>
  <div id="sidebar" class="horizontal-bar sidebar">
    <!-- Page Sidebar -->
  </div>

  <div class="page-inner">
    <div id="pageHeader" class="page-title">
      <!-- Page Header -->
    </div>

    <div id="main-wrapper">
    </div><!-- Main Wrapper -->

    <div id="footer" class="page-footer">
      <!-- footer -->
    </div>
  </div><!-- Page Inner -->
</main><!-- Page Content -->

<div class="cd-overlay"></div>

<!-- Javascripts -->
<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<script src="${basePath}${basePathEmpty}_static/js/config.js"></script>
<script data-main="${basePath}${basePathEmpty}_static/js/${dataMain}" src="${basePath}${basePathEmpty}_static/assets/plugins/require.js"></script>
<!-- END JAVASCRIPTS -->
</body>
</html>