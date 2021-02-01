<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>    
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no">
<title>배송 정보 수정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath }/order9/_order9_static/assets/css/reset.css">
<link rel="stylesheet" href="${pageContext.request.contextPath }/order9/_order9_static/assets/css/style.css">
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<!-- 폰트어썸 -->
<script src="https://kit.fontawesome.com/6f2f0f2d95.js"></script>
<!-- handlebars -->
<script src="https://cdn.jsdelivr.net/npm/handlebars@latest/dist/handlebars.js"></script>
<script>
	$(document).ready(function() {
		$(".orderItemInfoWrap").hide();
		
 		$(".titleBtn").click(function (e) {
			if($(".orderItemInfoWrap").hasClass("open")){
				$(".orderItemInfoWrap").slideUp();
				$(".orderItemInfoWrap").removeClass("open");
				$(this).html('<i class="fas fa-angle-down"></i>');
			}else{
				$(".orderItemInfoWrap").slideDown();
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
	    
	    //등록버튼
	    $("#registerBtn").click(function () {
			var name = $("input[name='cusName']").val();
			var cusCellNo = $("input[name='cusCellNo']").val();
			var cusTelNo = $("input[name='cusTelNo']").val(); 
			var cusAddress = "["+ $("input[name='cusZip']").val() + "]" +"\n"
			                  + "　" + $("#cusAddr1").val() + $("#cusAddr2").val(); 
			var cusMessage = $("#cusMessage").val(); 
			alert("입력하신 내용이 맞습니까?"+"\n"
					+" ● 이름 : "+name+"\n"
					+" ● 휴대전화 번호 : "+cusCellNo+"\n"
					+" ● 일반전화 번호 : "+cusTelNo+"\n"
					+" ● 주소"+"\n"
					+"　: "+cusAddress+"\n"
					+" ● 배송 메세지 : "+cusMessage);
		})
		
	    //취소버튼
	    $("#cancelBtn").click(function(){
	    	var res = confirm("수정을 취소하시겠습니까?");
	    	console.log(res);
	    	
	    	if(res){
	    		location.href = "${pageContext.request.contextPath }/order9/orderInfo?orderNo=${customerInfo.orderNo}";
	    	}
	    })
		
	  });
</script>
<style>
.orderItemInfoWrap{
	margin-top: 16px;
}
 .form .required label, .form .unrequired label{
    padding-left: 11px;
}

 .form .required label:before {
    content: '';
    width: 4px;
    height: 4px;
    background: #CC1C0A;
    border-radius: 50%;
    position: absolute;
    left: 0;
    top: 4px;
}
/*  .ordering_info h3 {
    text-align: right;
    position:relative;
} */

/* .ordering_info h3:before {
    content: '';
    width: 200%;
    height: 1px;
    background: #E0E4EA;
    position: absolute;
    left: -100%;
    bottom: -20px;
} */

 .shipping_info article:before {
    content: '';
    width: 4px;
    height: 4px;
    border-radius: 50%;
    background: #CC1C0A;
    position: absolute;
    right: 140px;
    top: -29px;
}

 .shipping_info article:after {
    content: '필수 입력 항목입니다.';
    color: #48494D;
    font-size: 14px;
    position: absolute;
    right: 0;
    top: -35px;
    margin-right: 10px;
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
#registerBtn{
	background: #CC1C0A;
	margin-right: 2%;
}
#cancelBtn{
	background: #262626;
	margin-left: 2%;
}

.form input::placeholder,
.form textarea::placeholder {
    color: #D4D8DD;
}
</style>
</head>
<body>
     <header>
        <div class="container">
            <h1 class="logo"><img src="${pageContext.request.contextPath }/order9/_order9_static/assets/img/logo.svg" alt="로고"></h1>
            <h2>배송 정보 수정</h2>
        </div>
    </header>
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
				<a href="#" class="titleBtn"> <i class="fas fa-angle-down"></i></a>
				</a>
			</div>
			
			<div class="orderItemInfoWrap" class="open">
            <article>
	            <c:forEach var="orderItem" items="${orderItemInfo}" >
	                <div class="list">
	                    <c:forEach var="itemImg" items="${orderItemImg}">
	                         <c:if test="${itemImg.itemId == orderItem.itemId}">
	                    		<img src="${itemImg.smallImage}" alt="상품 이미지" class="itemImg">
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
                        <label for="cusName" class="">이름</label>
                        <input type="text" name="cusName" placeholder="배송 받으실  정확한 성함을 입력해주세요" value="${customerInfo.cusName}">
                    </p>
                    <p class="required">
                        <label for="cusCellNo">휴대전화 번호</label>
                        <input type="tel" name="cusCellNo" placeholder="(-)없이 숫자만 입력해주세요" value="${customerInfo.cusCellNo}">
                    </p>
                    <p class="unrequired">
                        <label for="cusTelNo">일반전화 번호</label>
                        <input type="text" name="cusTelNo" placeholder="(-)없이 숫자만 입력해주세요" value="${customerInfo.cusTelNo}">
                    </p>
                    <p class="required">
                        <label for="cusZip">우편번호</label>
                        <input type="text" name="cusZip" placeholder="주소검색" value="${customerInfo.cusZip}">
                    </p>
                    <p class="required">
                        <label for="cusAddr1">주소</label>
                        <textarea type="text" id="cusAddr1" placeholder="주소" class="orderText2">${customerInfo.cusAddr1}</textarea>
                    </p>                    
                    <p class="required">
                        <label for="cusAddr2">상세주소</label>
                        <textarea type="text" id="cusAddr2" placeholder="상세주소" class="orderText">${customerInfo.cusAddr2}</textarea>
                    </p>
                    <p class="unrequired">
                        <label for="cusMessage">배송 메세지</label>
                        <textarea type="text" id="cusMessage" placeholder="배송메세지" class="orderText">${customerInfo.cusMessage}</textarea>
                    </p>
                    <div class="btnBox">
	                    <button type="submit" id="registerBtn" class="modifyBtn">등록</button>
	                    <button type="button" id="cancelBtn" class="modifyBtn">취소</button>
                    </div>
                </div>
            </article>
        </section>
        <footer>
            문의 전화 <em>1688-1580</em>
        </footer>
    </div>
    <script src="./_order9_static/assets/js/postcode.js"></script>
</body>

</html>