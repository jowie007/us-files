package de.us.web.payload.request;

import java.util.Objects;

public class SearchRequest {
	private String searchString;
	private int page;
	private int count;
	private String sortColumn;
	private String sortDirection;
	private SearchParamsRequest searchParams;
	private String userName;
	private boolean filterUnfinished;
	private boolean filterFavorites;

	public SearchRequest(String searchString, int page, int count, String sortColumn, String sortDirection,
			SearchParamsRequest searchParams, String userName, boolean filterUnfinished, boolean filterFavorites) {
		super();
		this.searchString = searchString;
		this.page = page;
		this.count = count;
		this.sortColumn = sortColumn;
		this.sortDirection = sortDirection;
		this.searchParams = searchParams;
		this.userName = userName;
		this.filterUnfinished = filterUnfinished;
		this.filterFavorites = filterFavorites;
	}

	public String getSearchString() {
		return searchString;
	}

	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getSortColumn() {
		return sortColumn;
	}

	public void setSortColumn(String sortColumn) {
		this.sortColumn = sortColumn;
	}

	public String getSortDirection() {
		return sortDirection;
	}

	public void setSortDirection(String sortDirection) {
		this.sortDirection = sortDirection;
	}

	public SearchParamsRequest getSearchParams() {
		return searchParams;
	}

	public void setSearchParams(SearchParamsRequest searchParams) {
		this.searchParams = searchParams;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public boolean isFilterUnfinished() {
		return filterUnfinished;
	}

	public void setFilterUnfinished(boolean filterUnfinished) {
		this.filterUnfinished = filterUnfinished;
	}

	public boolean isFilterFavorites() {
		return filterFavorites;
	}

	public void setFilterFavorites(boolean filterFavorites) {
		this.filterFavorites = filterFavorites;
	}

	@Override
	public int hashCode() {
		return Objects.hash(count, filterFavorites, filterUnfinished, page, searchParams, searchString, sortColumn,
				sortDirection, userName);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SearchRequest other = (SearchRequest) obj;
		return count == other.count && filterFavorites == other.filterFavorites
				&& filterUnfinished == other.filterUnfinished && page == other.page
				&& Objects.equals(searchParams, other.searchParams) && Objects.equals(searchString, other.searchString)
				&& Objects.equals(sortColumn, other.sortColumn) && Objects.equals(sortDirection, other.sortDirection)
				&& Objects.equals(userName, other.userName);
	}

	@Override
	public String toString() {
		return "SearchRequest [searchString=" + searchString + ", page=" + page + ", count=" + count + ", sortColumn="
				+ sortColumn + ", sortDirection=" + sortDirection + ", searchParams=" + searchParams + ", userName="
				+ userName + ", filterUnfinished=" + filterUnfinished + ", filterFavorites=" + filterFavorites + "]";
	}

}
