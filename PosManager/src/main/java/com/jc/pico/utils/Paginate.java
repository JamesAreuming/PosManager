package com.jc.pico.utils;

public class Paginate {
	public static final int PAGE_PER_SIZE = 30;
	
	StringBuffer bordPageInfo = new StringBuffer();	// 페이지에 표시될 HTML 구문
	long totalCount = 0;		// 전체 게시물 수
	long startList = 0;		// 현재 페이지의 첫번째 게시물번호
	long endList = 0;			// 현재 페이지의 마지막 게시물번호
	int startPage = 0;		// 현재 블럭의 시작 페이지
	int endPage = 0;			// 현재 블럭의 끝 페이지
	int prevPage = 0;			// 이전 블럭의 마지막 페이지
	int nextPage = 0;			// 다음 블럭의 첫번째 페이지
	int currentPage = 0;		// 현재 페이지 번호
	int listPerPage = PAGE_PER_SIZE; 		// 페이지당 게시물 수
	int totalPage = 0;		// 전체 페이지 수
	int pagePerBlock = 0;	// 블럭당 페이지 수
	int totalBlock = 0;		// 전체 블럭 수
	int currentBlock = 0;		// 현재 블럭 번호
	String keyword = "";
	String keytype = "";
	boolean prev = false;	
	boolean next = false;
	
	/**
	 * 
	 * @param ttList - 전체 게시물 수
	 * @param noPage - 현재 페이지 번호
	 * 
	 * 전체 게시물 수와 현재 페이지 번호만 받는 생성자
	 * 페이지당 게시물 수 = 10, 블럭당 페이지수 = 10 으로 자동 할당
	 * 
	 */
	public Paginate(long ttList, int noPage) {
		this.totalCount = ttList;
		this.currentPage = noPage;
		this.listPerPage = 10;
		this.pagePerBlock = 10;
		
		setPageDivInfo();
	}
	
	/**
	 * 
	 * @param ttList - 전체 게시물 수
	 * @param noPage - 현재 페이지 번호
	 * @param szPage - 페이지당 게시물 수 
	 * @param szBloc - 블럭당 페이지 수
	 * 
	 * 게시물 분리에 필요한 기본 요소를 모두 받음
	 */
	
	public Paginate(long ttList, int noPage, int szPage, int szBloc, String keyword) {
		this.totalCount = ttList;
		this.currentPage = noPage;
		this.listPerPage = szPage;
		this.pagePerBlock = szBloc;
		this.keyword = keyword;
		
		setPageDivInfo();
	}
	
	public Paginate(long ttList, int noPage, int szPage, int szBloc, String keyword, String keytype) {
		this.totalCount = ttList;
		this.currentPage = noPage;
		this.listPerPage = szPage;
		this.pagePerBlock = szBloc;
		this.keyword = keyword;
		this.keytype = keytype;
		
		setPageDivInfo();
	}
	
	public Paginate(long ttList, int noPage, int szPage, int szBloc) {
		this.totalCount = ttList;
		this.currentPage = noPage;
		this.listPerPage = szPage;
		this.pagePerBlock = szBloc;
		
		setPageDivInfo();
	}
	
	public Paginate(long ttList, int noPage, int szPage) {
		this.totalCount = ttList;
		this.currentPage = noPage;
		this.listPerPage = szPage;
		
		setPageDivInfo();
	}
	
	/**
	 * 페이지 분리 정보를 셋팅함
	 * 객체 생성 후 기본 정보가 변경되었을 때에 본 메서드를 실행해야 함
	 * 기본 정보 : ttLIst, noPage, szPage, szBloc
	 */
	public void setPageDivInfo() {
		setTotalPage();
		setTotalBlock();
		setCurrentBlock();
		setStartPage();
		setEndPage();
		setPrevPage();
		setNextPage();
		setStartList();
		setEndList();
		setPrev();
		setNext();
	}

	public void setBordPageInfo(StringBuffer bordPageInfo) {this.bordPageInfo = bordPageInfo;}
	
	/**
	 * @param ttList - 전체 게시물 수
	 * 전체 게시물 수 설정
	 */
	public void setTotalCount(int ttList) {this.totalCount = ttList;}
	
	/**
	 * 현재 페이지의 시작 게시물 번호 셋
	 * (현재페이지 번호 + 페이지당 게시물수) - (페이지당 게시물수 - 1 )
	 */
	private void setStartList() {
		this.startList = (this.currentPage * this.listPerPage) - (this.listPerPage - 1);
	}
	
	/**
	 * 현재 페이지의 마지막 게시물 번호 셋
	 * 현재 페이지 번호 * 페이지당 게시물 수
	 */
	private void setEndList() {
		this.endList = this.currentPage * this.listPerPage;
		
		if (endList > totalCount) endList = totalCount;
	}
	
	/**
	 * 현재 블럭의 시작 페이지 번호 셋
	 * 블럭당 페이지 수 * 현재 블럭 번호 - (블럭당 페이지 수 - 1)
	 */
	private void setStartPage() {
		this.startPage = this.pagePerBlock * this.currentBlock - (this.pagePerBlock - 1);
	}
	
	/**
	 * 현재 블럭의 마지막 페이지 번호 셋
	 * 블럭당 페이지 수 * 블럭 번호
	 * 단, 전체 페이지 수가 마지막 페이지 번호 보다 낮다면 전체 페이지 수
	 */
	private void setEndPage() {
		this.endPage = this.pagePerBlock * this.currentBlock;
		if (this.totalPage < this.endPage) this.endPage = this.totalPage;
	}
	
	/**
	 * prev 버튼 클릭시 이동할 페이지 번호
	 */
	private void setPrevPage() {
		if (this.currentPage > 1) {
			this.prevPage = this.currentPage - 1; 
		} else {
			this.prevPage = 1;
		}

	}
	
	/**
	 * next 버튼 클릭시 이동할 페이지 번호
	 */
	private void setNextPage() {
		
		if (this.currentPage < this.totalPage) {
			this.nextPage = this.currentPage + 1;
		} else {
			this.nextPage = this.currentPage;
		}
	}
	
	/**
	 * 
	 * @param noPage 현재 페이지 번호
	 * 현재 페이지 번호 셋
	 */
	public void setCurrentPage(int noPage) {this.currentPage = noPage;}
	
	/**
	 * 
	 * @param szPage 페이지당 게시물 숫
	 * 페이지당 게시물 수 셋
	 */
	public void setListPerPage(int szPage) {this.listPerPage = szPage;}
	
	/**
	 * 총 페이지 수 셋
	 */
	private void setTotalPage() {
		if (this.totalCount <= this.listPerPage) {
			this.totalPage = 1;
		} else {
			this.totalPage = (int) Math.ceil((float)this.totalCount / this.listPerPage);
		}
	}
	
	/**
	 * 
	 * @param szBloc 블럭당 페이지 수
	 * 블럭당 페이지 수 셋
	 */
	public void setPagePerBloc(int szBloc) {this.pagePerBlock = szBloc;}
	
	/**
	 * 총 블럭 수 셋
	 */
	private void setTotalBlock() {
		if (this.totalCount <= this.listPerPage) {
			this.totalBlock = 1;
		} else {
			this.totalBlock = (int) Math.ceil((float)this.totalPage / this.pagePerBlock);
		}
	}
	
	/**
	 * 현재 블럭 번호 셋
	 */
	private void setCurrentBlock() {
		this.currentBlock = (int)Math.ceil((float)this.currentPage / this.pagePerBlock);
	}
	
	/**
	 * 이전 페이지의  존재 여부 셋
	 */
	public void setPrev() {
		if (this.currentPage != 1) {
			this.prev = true;
		} else {
			this.prev = false;
		}
	}
	
	/**
	 * 다음 페이지 존재 여부 셋
	 */
	public void setNext() {
		if (this.currentPage < this.totalPage)
			this.next = true;
		else 
			this.next = false;
	}
	
	
	public StringBuffer getBordPageInfo() {return bordPageInfo;}
	public long getTotalCount() {return totalCount;}
	public long getStartList() {return startList;}
	public long getEndList() {return endList;}
	public int getStartPage() {return startPage;}
	public int getEndPage() {return endPage;}
	public int getPrevPage() {return prevPage;}
	public int getNextPage() {return nextPage;}
	public int getCurrentPage() {return currentPage;}
	public int getListPerPage() {return listPerPage;}
	public int getTotalPage() {return totalPage;}
	public int getPagePerBloc() {return pagePerBlock;}
	public int getTotalBlock() {return totalBlock;}
	public int getCurrentBlock() {return currentBlock;}
	public boolean hasPrev() {return prev;}
	public boolean hasNext() {return next;}
	public boolean isPrev() {return prev;}
	public boolean isNext() {return next;}

	@Override
	public String toString() {
		return "Paginate [bordPageInfo=" + bordPageInfo + ", totalCount=" + totalCount + ", startList=" + startList
				+ ", endList=" + endList + ", startPage=" + startPage + ", endPage=" + endPage + ", prevPage="
				+ prevPage + ", nextPage=" + nextPage + ", currentPage=" + currentPage + ", listPerPage=" + listPerPage
				+ ", totalPage=" + totalPage + ", pagePerBlock=" + pagePerBlock + ", totalBlock=" + totalBlock
				+ ", currentBlock=" + currentBlock + ", keyword=" + keyword + ", keytype=" + keytype + ", prev=" + prev
				+ ", next=" + next + "]";
	}
}
