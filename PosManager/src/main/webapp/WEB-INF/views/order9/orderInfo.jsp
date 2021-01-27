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
<link rel="stylesheet" href="./_order9_static/assets/css/reset.css">
<link rel="stylesheet" href="./_order9_static/assets/css/style.css">
<!-- jQuery -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script>
	//textarea 자동조절
	$(document).ready(function() {
	    $('.required').on( 'keyup', 'textarea', function (e){
	      $(this).css('height', '44px' );
	      $(this).height( this.scrollHeight );
	    });
	    $('.required').find( 'textarea' ).keyup();
	    
	    var s = $("em.count").val();
		console.log(s);
	    
	    
	  });
</script>
</head>
<body>
    <header>
        <div class="container">
            <h1 class="logo"><img src="./_order9_static/assets/img/logo.svg" alt="로고"></h1>
            <h2>주문 상세 정보</h2>
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
			<h3>주문 상품</h3>
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
                    <div class="error">
                        <p>에러 메세지 입력</p>
                    </div>
                    <button type="submit">배송 정보 수정</button>
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