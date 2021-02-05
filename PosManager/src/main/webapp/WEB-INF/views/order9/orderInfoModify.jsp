<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="includeHeader.jsp" %>  
<script>
	$(document).ready(function() {
		
		//orderInfo/modify - 주문상품 접기(default)
		$(".orderItemInfoWrap").hide();
		
		//주문상품 슬라이드 버튼
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
	    
	    //오늘날짜, 주문한날짜
    	var today = new Date();  // Mon Feb 01 2021 11:51:16 GMT+0900 (대한민국 표준시)
    	var orderDate = "${formatOrderDate}"; //2021-01-27
    	
    	var orderArray = orderDate.split('-');
        var order_date = new Date(orderArray[0],Number(orderArray[1])-1,orderArray[2]); //Wed Jan 27 2021 00:00:00 GMT+0900 (대한민국 표준시)
        
        //오늘날짜 ~ 주문한날짜 일수 구하기 : 주문할날짜 기준으로부터 3일 지나면 배송정보수정 막기
        var betweenDay = Math.round((today.getTime() - order_date.getTime())/1000/60/60/24);  
        
	    var orderSt = ${customerInfo.svcOrder.orderSt};
	    
		//등록버튼 -- input 검사 후 등록
	 	$("form").submit(function(){
			var cusName = $("input[name='cusName']").val();
			var cusCellNo = $("input[name='cusCellNo']").val();
			var cusTelNo = $("input[name='cusTelNo']").val();
			var cusZip = $("input[name='cusZip']").val();
			var cusAddr1 = $("textarea[name='cusAddr1']").val(); 
			var cusAddr2 = $("textarea[name='cusAddr2']").val(); 
			
			var cusAddress = "["+ cusZip + "]" +"\n"
                             + "　" + cusAddr1 + cusAddr2; 
			var cusMessage = $("textarea[name='cusMessage']").val();
			
			var orderInfo = "배송받으실 정보가 맞습니까?"+"\n"
							+" ● 이름 : "+cusName+"\n"
							+" ● 휴대전화 번호 : "+cusCellNo+"\n"
							+" ● 주소"+"\n"
							+"　: "+cusAddress+"\n";
			
			//필수 입력 항목이 비워있는 경우
			if(cusName == "" || cusCellNo == "" || cusZip == "" || cusAddr1 == "" || cusAddr2 == "" ){
				alert("필수 입력 항목 입니다.");
				return false;
			}
			
			//수정 일자 초과한 경우
	        if(betweenDay > 3){ //ex. 2월1일 주문 - 2월3일까지 수정가능 / 2월4일부터 주문정보수정 X
	        	alert("배송 정보 수정 일자가 초과하였습니다.");
	        	return false;
	        }
			
	        if(orderSt == "607003"){
	        	$("#registerBtn").css("background","#8F9399");
		    	$("#registerBtn").text("주문 취소");
	        	alert("주문 취소된 내역이므로 수정하여 등록할 수 없습니다.");
	        	return false;
	        }
			
			var res = confirm(orderInfo);
			
			if(res){
				
			}else{
				//취소버튼시 막기
				return false;
			}
	        
		})
		$("#zipSearchBtn").click(function(){
			$("input[name='cusZip']").val("");
			$("textarea[name='cusAddr1']").val(""); 
			$("textarea[name='cusAddr2']").val(""); 
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
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    function openDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 각 주소의 노출 규칙에 따라 주소를 조합한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var addr = ''; // 주소 변수
                var extraAddr = '';
                //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
                if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                    addr = data.roadAddress;
                	zon = data.zonecode;
                	console.log("확인");
                	console.log(addr);
                    console.log(zon);
                } else { // 사용자가 지번 주소를 선택했을 경우(J)
                    addr = data.jibunAddress;
                }

                // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
                 if(data.userSelectedType === 'R'){
                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                        extraAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if(data.buildingName !== '' && data.apartment === 'Y'){
                        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if(extraAddr !== ''){
                        extraAddr = ' (' + extraAddr + ')';
                    }
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.

                document.getElementById("cusZip").value = data.zonecode;
                document.getElementById("cusAddr1").value = addr + "" +extraAddr;
                // 커서를 상세주소 필드로 이동한다.
                document.getElementById("cusAddr2").focus();
            }
        }).open();
    }
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
.point{
	color: #CC1C0A;
	font-size: 10px;
	vertical-align: unset;
}
#zipSearchBtn{
	background: #CC1C0A;
	color: white;
	border-radius: 5px;
	padding: 6px;
	margin-left: 3px;
	font-size: 12px;	
}
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
					<a href="#" class="titleBtn"> <i class="fas fa-angle-down"></i></a>
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
        
        <!-- section(2) -->
        <section class="shipping_info">
            <h3> 배송 정보  </h3>
            <article> 
                <div class="form">
                	<form action="orderNoSearch" method="post">
                	<input type="hidden" name="orderNo" value="${customerInfo.orderNo}">
                    <p class="required">
                        <label for="cusName">이름</label>
                        <input type="text" name="cusName" placeholder="성함을 입력해 주세요" value="${customerInfo.cusName}">
                    </p>
                    <p class="required">
                        <label for="cusCellNo">휴대전화 번호</label>
                        <!-- input type : number, pattern = \d* 숫자패드 바로나오게 설정 -->
                        <input type="number" name="cusCellNo" pattern = "\d*" placeholder="(-)없이 숫자만 입력해주세요" value="${customerInfo.cusCellNo}">
                    </p>
                    <p class="unrequired">
                        <label for="cusTelNo">일반전화 번호</label>
                        <input type="number" name="cusTelNo" pattern = "\d*" placeholder="(-)없이 숫자만 입력해주세요" value="${customerInfo.cusTelNo}">
                    </p>
                    <p class="required">
                        <label for="cusZip">우편번호</label>
                        <button type="button" id="zipSearchBtn" onclick="openDaumPostcode()">우편번호 검색</button>                    
                        <input type="number" name="cusZip" id="cusZip" pattern = "\d*"  placeholder="우편번호" value="${customerInfo.cusZip}" readonly="readonly">
                    </p>
                    <p class="required">
                        <label for="cusAddr1">주소</label>
                        <textarea type="text" name="cusAddr1" id="cusAddr1" placeholder="주소" class="orderText2" readonly="readonly">${customerInfo.cusAddr1}</textarea>
                    </p>                    
                    <p class="required">
                        <label for="cusAddr2">상세주소</label>
                        <textarea type="text" name="cusAddr2" id="cusAddr2" placeholder="상세주소" class="orderText">${customerInfo.cusAddr2}</textarea>
                    </p>                   
                    <p class="unrequired">
                        <label for="cusMessage">배송 메세지</label>
                        <textarea type="text" name="cusMessage" id="cusMessage" placeholder="배송메세지를 입력해 주세요" class="orderText">${customerInfo.cusMessage}</textarea>
                    </p>
                    <div class="btnBox">
	                    <button type="submit" id="registerBtn" class="modifyBtn">등록</button>
	                    <button type="button" id="cancelBtn" class="modifyBtn">취소</button>
                    </div>
                    </form>
                </div>
            </article>
        </section>
      </div>  
<%@ include file="includeFooter.jsp" %>           