<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includeHeader.jsp" %>
    
<script>
	$(document).ready(function() {
		
		//orderInfo - 주문상품 펼치기(default)
		$(".orderItemInfoWrap").show();
		
		//주문상품 슬라이드 버튼
		$(".titleBtn").click(function (e) {
			if($(".orderItemInfoWrap").hasClass("open")){
				$(".orderItemInfoWrap").slideDown();
				$(".orderItemInfoWrap").removeClass("open");
				$(this).html('<i class="fas fa-angle-up"></i>');
			}else{
				$(".orderItemInfoWrap").slideUp();
				$(".orderItemInfoWrap").addClass("open");
				$(this).html('<i class="fas fa-angle-down"></i>');
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
        
        var orderSt = ${customerInfo.svcOrder.orderSt};
	    //배송정보수정버튼
	    $("#modifyInfoBtn").click(function(){
	        if(betweenDay > 3){ //ex. 2월1일 주문 - 2월3일까지 수정가능 / 2월4일부터 주문정보수정 X
	        	alert("배송 정보 수정일자가 초과하였습니다.");
	        }else if(orderSt == "607003"){
	        	$("#modifyInfoBtn").css("background","#8F9399");
		    	$("#modifyInfoBtn").text("주문 취소");
		    	alert("주문 취소된 내역이므로 수정할 수 없습니다.");
	        }else{ //배송정보수정 페이지 이동
	        	location.href = "${pageContext.request.contextPath }/order9/orderInfo/modify?orderNo=${customerInfo.orderNo}";	
	        }	    	
	    })
	    
	  });
</script>
<style>
.orderItemInfoWrap{
	margin-top: 16px;
}
/* .itemOptBox{ 
	width: 100%;
	padding-bottom: 9px;
}
.itemOpt{
	padding: 5px 0 0 8px;
	font-size: 11px;
	color: #777A7F;
}
.itemOpt:nth-child(1){
	padding-top: 0;
} */
</style>    

    <div class="container">
        <!-- section(1) -->
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
			                        <div class="itemOptBox">
									 <c:forEach var="orderItemOpt" items="${orderItemOptInfo}">
										 	<c:if test="${orderItem.id == orderItemOpt.itemId}">
											 	  <p class = "itemOpt">* ${orderItemOpt.optNm} | 
											 	                       ${orderItemOpt.optDtlNm} 
											 	                       <c:if test ="${orderItemOpt.optPrice != 0.0}">
											 	                          (+<fmt:formatNumber type="number" maxFractionDigits="3" value="${orderItemOpt.optPrice}"/>)									 	                         
											 	                        </c:if>
											 	                        </p>
											 	                        
										 	</c:if> 									 	
							        </c:forEach>
							        </div>				         		                        
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
        
        <!-- section(2) -->
        <section class="shipping_info">
            <h3> 배송 정보  </h3>
            <article> 
                <div class="form">
                    <p class="required">
                        <label for="cusName">이름</label>
                        <input type="text" name="cusName" placeholder="미입력" value="${customerInfo.cusName}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusCellNo">휴대전화 번호</label>
                        <input type="tel" name="cusCellNo" placeholder="미입력" value="${customerInfo.cusCellNo}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusTelNo">일반전화 번호</label>
                        <input type="number" name="cusTelNo" placeholder="미입력" value="${customerInfo.cusTelNo}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusZip">우편번호</label>
                        <input type="number" id="cusZip" placeholder="미입력" value="${customerInfo.cusZip}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusAddr1">주소</label>
                        <textarea type="text" id="cusAddr1" placeholder="미입력" readonly="readonly" class="orderText2">${customerInfo.cusAddr1}</textarea>
                    </p>                    
                    <p class="required">
                        <label for="cusAddr2">상세주소</label>
                        <textarea type="text" id="cusAddr2" placeholder="미입력" readonly="readonly" class="orderText">${customerInfo.cusAddr2}</textarea>
                    </p>
                    <p class="required">
                        <label for="cusMessage">배송 메세지</label>
                        <textarea type="text" id="cusMessage" placeholder="미입력" readonly="readonly" class="orderText">${customerInfo.cusMessage}</textarea>
                    </p>
                    <div class="btnBox">                    
	                    <button type="button" id="modifyInfoBtn" class="oneBtn">배송 정보 수정</button>
                    </div>
                </div>
            </article>
        </section>        
    </div>

<%@ include file="includeFooter.jsp" %>     