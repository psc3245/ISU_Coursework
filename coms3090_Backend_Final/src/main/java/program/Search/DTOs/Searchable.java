package program.Search.DTOs;

public class Searchable {
    private Object data;
    private SearchTypes searchType;

    public Searchable(Object data, SearchTypes searchType) {
        this.data = data;
        this.searchType = searchType;
    }
    public Object getData() {
        return data;
    }
    public void setData(Object data) {
        this.data = data;
    }
    public SearchTypes getSearchType() {
        return searchType;
    }
    public void setSearchType(SearchTypes searchType) {
        this.searchType = searchType;
    }
}
