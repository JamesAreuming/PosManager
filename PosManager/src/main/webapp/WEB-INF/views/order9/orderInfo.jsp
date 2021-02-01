<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>주문 상세 정보</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/order9/_order9_static/assets/css/reset.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/order9/_order9_static/assets/css/style.css">
<!-- 폰트어썸 -->
<script src="https://kit.fontawesome.com/6f2f0f2d95.js"></script>
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
	$(document).ready(function() {
		$(".orderItemInfoWrap").show();
		
		$(".titleBtn").click(function (e) {
			if($(".orderItemInfoWrap").hasClass("open")){
				$(".orderItemInfoWrap").slideDown();
				$(".orderItemInfoWrap").removeClass("open");
				$(this).html('<i class="fas fa-angle-down"></i>');
			}else{
				$(".orderItemInfoWrap").slideUp();
				$(".orderItemInfoWrap").addClass("open");
				$(this).html('<i class="fas fa-angle-up"></i>');
			}
		})
		
		//textarea 자동조절
	    $(".required").on( "keyup", "textarea", function (e){
	      $(this).css("height", "44px" );
	      $(this).height( this.scrollHeight );
	    });
	    $(".required").find("textarea").keyup();
	    
	    
	    //오늘날짜, 주문한날짜
    	var today = new Date();  // Mon Feb 01 2021 11:51:16 GMT+0900 (대한민국 표준시)
    	var orderDate = "${formatOrderDate}"; //2021-01-27
    	
    	var orderArray = orderDate.split('-');
        var order_date = new Date(orderArray[0],Number(orderArray[1])-1,orderArray[2]); //Wed Jan 27 2021 00:00:00 GMT+0900 (대한민국 표준시)
        
        //오늘날짜 ~ 주문한날짜 일수 구하기 : 주문할날짜 기준으로부터 3일 지나면 배송정보수정 막기
        var betweenDay = Math.round((today.getTime() - order_date.getTime())/1000/60/60/24);  
        
        //alert(betweenDay);
        
        
	    //배송정보수정btn
	    $("#modifyInfoBtn").click(function(){
	        if(betweenDay > 3){ //ex. 2월1일 주문 - 2월3일까지 수정가능 / 2월4일부터 주문정보수정 X
	        	alert("주문하지 3일이 지나 수정하실 수 없습니다.");
	        }else{
	        	location.href = "${pageContext.request.contextPath }/order9/orderInfo/modify?orderNo=${customerInfo.orderNo}";	
	        }	    	
	    })
	    
	    //주문취소btn
	    $("#orderCancelBtn").click(function () {
	    	var res = confirm("주문을 취소하시겠습니까?");
	        if(betweenDay > 3){ //ex. 2월1일 주문 - 2월3일까지 수정가능 / 2월4일부터 주문정보수정 X
	        	alert("주문하지 3일이 지나 주문 취소할 수 없습니다.");
	        }else{
	        	if(res){
	        		location.href = "${pageContext.request.contextPath }/order9/orderInfo/modify?orderNo=${customerInfo.orderNo}";
	        	}
	        }	   
		})
	    
	  });
</script>
<style>
.orderItemInfoWrap{
	margin-top: 16px;
}
/* .form button {
    position: absolute;
    left: 0;
    bottom: -73px;
    height: 56px;
    border-radius: 8px;
    background: #CC1C0A;
    color: #fff;
    font-size: 18px;
    width: 100%;
}
 */
.modifyBtn:hover {
	background-color: #BF1200;
}

.form input::placeholder,
.form textarea::placeholder {
    color: #333333;
}
div.btnBox{
	position: absolute;
    left: 0;
    bottom: -56px;
    width: 100%;
    height: 56px;
}
.modifyBtn{
	height: 56px;
	border-radius: 8px;
	color: #fff;
	font-size: 18px;
	width: 48%;
	float: left;
}
#modifyInfoBtn{
	background: #CC1C0A;
	margin-right: 2%;
}
#orderCancelBtn{
	background: #262626;
	margin-left: 2%;
}
</style>
</head>
<body>
    <!-- header -->
    <header>
        <div class="container">
            <h1 class="logo"><img src="${pageContext.request.contextPath }/order9/_order9_static/assets/img/logo.svg" alt="로고"></h1>
            <h2>주문 상세 정보</h2>
        </div>
    </header>
    <!-- section -->
    <div class="container">
        <section class="ordering_info">
        
			<div class="info">
				<span>
					<h4>주문 번호</h4>
					<p>${customerInfo.orderNo}</p>
				</span> <span>
					<h4>영수 번호</h4>
					<p>${customerInfo.svcOrder.receiptNo}</p>
				</span>
			</div>
			
			<div class="orderItemWrap">
			<div class="titleBox">
				<span class="title">주문 상품</span>
				<a href="#" class="titleBtn"> <i class="fas fa-angle-up"></i></a>
				</a>
			</div>
			
			<div class="orderItemInfoWrap" class="open">
			<article>
	            <c:forEach var="orderItem" items="${orderItemInfo}" >
	                <div class="list">
	                    <c:forEach var="itemImg" items="${orderItemImg}">
	                         <c:if test="${itemImg.itemId == orderItem.itemId}">
	                    		<img src="${itemImg.smallImage}" alt="상품 이미지">
	                    	 </c:if>
	                    </c:forEach>
	                    <div class="list_info">
	                        <h4>${orderItem.itemNm}</h4>
	                        <span>수량 <em>${orderItem.count}</em></span>
	                        <span>
	                             <em>
	                                <fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItem.sales}"/>
	                             </em>원
	                        </span>
	                    </div>
	                </div>
	            </c:forEach>
            </article>
            
            <article class="total">
                <span>총 <em>${totalCount}</em>건</span>
                <span>합계 
                 <em>
                    <fmt:formatNumber type="number" maxFractionDigits="3" value="${customerInfo.svcOrder.sales}" />
                  </em>원
                </span>
            </article>
            </div>
            </div>
        </section>
        
        <section class="shipping_info">
             <h3> 배송 정보  </h3>
            <article> 
                <div class="form">
                    <p class="required">
                        <label for="cusName">이름</label>
                        <input type="text" name="cusName" placeholder="이름" value="${customerInfo.cusName}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusCellNo">휴대전화 번호</label>
                        <input type="tel" name="cusCellNo" placeholder="휴대전화 번호"  value="${customerInfo.cusCellNo}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusTelNo">일반전화 번호</label>
                        <input type="text" name="cusTelNo" placeholder="일반전화 번호" value="${customerInfo.cusTelNo}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusZip">우편번호</label>
                        <input type="text" id="cusZip" placeholder="우편번호 찾기" value="${customerInfo.cusZip}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusAddr1">주소</label>
                        <textarea type="text" id="cusAddr1" placeholder="주소" readonly="readonly" class="orderText2">${customerInfo.cusAddr1}</textarea>
                    </p>                    
                    <p class="required">
                        <label for="cusAddr2">상세주소</label>
                        <textarea type="text" id="cusAddr2" placeholder="상세주소" readonly="readonly" class="orderText">${customerInfo.cusAddr2}</textarea>
                    </p>
                    <p class="required">
                        <label for="cusMessage">배송 메세지</label>
                        <textarea type="text" id="cusMessage" placeholder="배송 메세지" readonly="readonly" class="orderText">${customerInfo.cusMessage}</textarea>
                    </p>
                    <div class="btnBox">                    
	                    <button type="button" id="modifyInfoBtn" class="modifyBtn">배송 정보 수정</button>
	                    <button type="button" id="orderCancelBtn" class="modifyBtn">주문 취소</button>
                    </div>
                </div>
            </article>
        </section>
    </div>

    <!-- footer -->
	<footer>
		문의 전화 <em>1688-1580</em>
	</footer>
</body>

</html>