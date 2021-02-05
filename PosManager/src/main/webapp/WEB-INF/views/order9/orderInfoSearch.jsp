<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includeHeader.jsp" %>
    
<script>
	$(document).ready(function() {
		var orderNo = $("input[name='orderNo']").val();
		
		//orderNo없을시 info박스 숨기기, input창 수정가능
		if(orderNo ==""){
			$(".info").hide();
			$("input[name='orderNo']").attr('readonly', false);
		}
		
		//조회버튼
		$("#searchBtn").click(function () {
			if(orderNo ==""){
				alert("주문번호를 입력해 주세요.");
			}else{
				location.href = "${pageContext.request.contextPath }/order9/orderInfo?orderNo="+orderNo;
			}		
		})
	    
	  });
</script>
<style>
.modify_message{
	text-align: center;
}
.modify_message h3:nth-child(1) {
	color: #48494D;
	margin-bottom: 10px;
	font-size: 18px;
	letter-spacing: 1px;
}
.modify_message h3:nth-child(2) {
	font-weight: normal;
	font-size: 16px;
	color: #8F9399;
}
.modify_message .red{
	color: #CC1C0A;
	display: inline;
}
</style>   
 
    <!-- section -->
    <div class="container">
        <section class="ordering_info">
			<div class="info">
				<span class="modify_message">
					<h3>배송 정보가 <span class="red">저장</span>되었습니다.</h3>
					<h3>이용해 주셔서 감사합니다.</h3>
				</span>
			</div>
        </section>
        
        <section class="shipping_info">
             <h3>주문 조회</h3>
            <article> 
                <div class="form">
                    <p class="required">
                        <label for="cusName">주문 번호 조회</label>
                        <input type="number" pattern = "\d*" name="orderNo" placeholder="주문번호를 입력해 주세요." value="${orderNo}" readonly="readonly">
                    </p>
                    <div class="btnBox">                    
	                    <button type="button" id="searchBtn" class="oneBtn">조회</button>
                    </div>
                </div>
            </article>
        </section>        
    </div>

<%@ include file="includeFooter.jsp" %>     