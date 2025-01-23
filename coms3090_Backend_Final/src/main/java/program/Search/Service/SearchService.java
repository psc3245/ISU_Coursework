package program.Search.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import program.Groups.Group;
import program.Groups.GroupRepository;
import program.Groups.Service.GroupService;
import program.Groups.dto.GroupDTO;
import program.Posts.Post;
import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.Repositories.PostRepository;
import program.Posts.Service.PostService;
import program.Search.DTOs.Searchable;
import program.Users.User;
import program.Users.UserRepository;
import program.Users.dto.UserDTO;
import program.Search.DTOs.*;

@Service
public class SearchService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private PostRepository postRepository;

    public SearchService() {
    }

    public List<Searchable> getSearchablesBySubstring(String subString){
        ArrayList<Searchable> list = new ArrayList<Searchable>();
        getUserSearchables(subString,list);
        getGroupSearchables(subString, list);
        getPostSearchables(subString, list);
    
        return list;
    }
    public List<Searchable> getSearchablesBySubstringAndType(String subString, SearchTypes type){
        ArrayList<Searchable> list = new ArrayList<Searchable>();
        switch (type) {
            case POST:
                getPostSearchables(subString, list);
                break;
            case GROUP:
                getGroupSearchables(subString, list);
                break;
            case USER:
                getUserSearchables(subString, list);
                break;
            default:
                break;
        }
        return list;
    }
    private void getUserSearchables(String subString, ArrayList<Searchable> list){
        List<User> matchingUsers = userRepository.findAllByUsernameContainingIgnoreCase(subString);
        for (User user : matchingUsers) {
            list.add(new Searchable(UserDTO.turnToDTO(user), SearchTypes.USER));
        }

    }
    private void getPostSearchables(String subString, ArrayList<Searchable> list){
        List<Post> matchingPosts = postRepository.findAllByTitleContainingIgnoreCase(subString);
        for (Post post : matchingPosts) {
            list.add(new Searchable(PostService.convertPostToPostDTO(post), SearchTypes.POST));
        }

    }
    private void getGroupSearchables(String subString, ArrayList<Searchable> list){
        List<Group> matchingGroups = groupRepository.findAllByGroupNameContainingIgnoreCase(subString);
        for (Group group : matchingGroups) {
            list.add(new Searchable(GroupService.groupToGroupDTO(group), SearchTypes.GROUP));
        }
    }

    
}
