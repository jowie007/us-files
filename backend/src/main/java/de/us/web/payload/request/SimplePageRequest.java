package de.us.web.payload.request;

import java.util.Objects;

public class SimplePageRequest {

	private Integer page;
	private Integer size;

	public SimplePageRequest(Integer page, Integer size) {
		super();
		this.page = page;
		this.size = size;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public int hashCode() {
		return Objects.hash(page, size);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimplePageRequest other = (SimplePageRequest) obj;
		return Objects.equals(page, other.page) && Objects.equals(size, other.size);
	}

	@Override
	public String toString() {
		return "SimplePageRequest [page=" + page + ", size=" + size + "]";
	}

}
