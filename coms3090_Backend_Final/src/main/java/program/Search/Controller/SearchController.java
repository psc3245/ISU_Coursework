package program.Search.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import program.Search.DTOs.SearchTypes;
import program.Search.Service.SearchService;

@RestController
@RequestMapping("/api/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    
    private <T> ResponseEntity<?> handleRequest(RequestExecutor<T> executor) {
        try {
            return new ResponseEntity<>(executor.execute(), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @FunctionalInterface
    interface RequestExecutor<T> {
        T execute() throws Exception;
    }

    @GetMapping("/{searchString}")
    ResponseEntity<?> getSearchablesBySubstring(@PathVariable String searchString){
        return handleRequest(()-> searchService.getSearchablesBySubstring(searchString));
    }
    @GetMapping("/{searchString}/{type}")
    ResponseEntity<?> getSearchablesBySubstringAndType(@PathVariable String searchString, @PathVariable SearchTypes type){
        return handleRequest(()->searchService.getSearchablesBySubstringAndType(searchString, type));
    }
}
