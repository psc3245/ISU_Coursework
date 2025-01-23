package program.Groups;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import program.Groups.dto.GroupCreationRequest;
import program.Groups.dto.GroupDTO;
import program.Groups.dto.UpdateGroupsRequest;
import program.Posts.DTOs.PostDTOs.PostDTO;
import program.Posts.Post;
import program.Posts.Repositories.PostRepository;
import program.Posts.Service.PostService;
import program.Users.User;
import program.Groups.dto.AddMembersRequest;
import program.Groups.Service.GroupService;
import program.Users.UserRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Autowired
    public GroupController(GroupRepository groupRepository, GroupService groupService, PostRepository postRepository, UserRepository userRepository){
        this.groupRepository = groupRepository;
        this.groupService = groupService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/all")
    List<GroupDTO> getAllGroup() {
      return groupService.getGroups(groupRepository.findAll());
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<GroupDTO> getGroupbyID(@PathVariable Integer id) {
        try{
            return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupbyID(id));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping()
    ResponseEntity<String> createGroup(@RequestBody @Valid GroupCreationRequest group) {
        // if (group == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Request");
        // groupRepository.save(group);
        // return ResponseEntity.status(HttpStatus.OK).body("Group Created");
        try{
            groupService.createGroupRequest(group);
            return ResponseEntity.status(HttpStatus.OK).body("Group was created and user was added as member/owner");
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping()
    ResponseEntity<GroupDTO> updateGroup(@RequestBody @Valid UpdateGroupsRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(groupService.updateGroupRequest(request));
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<String> deleteGroup(@PathVariable Integer id) {
        groupRepository.deleteByGroupId(id);
        return ResponseEntity.status(HttpStatus.OK).body("group deleted");
    }
    @DeleteMapping(path = "/name/{groupName}")
    ResponseEntity<String> deleteGroupName(@PathVariable String groupName) {
        Group group = groupRepository.findByGroupName(groupName).get();
        // Delete posts when group is deleted
        for (Post p : group.getPosts() ) {
            postRepository.deleteByPostId(p.getPostId());
        }
        groupRepository.deleteByGroupId(group.getGroupId());
        return ResponseEntity.status(HttpStatus.OK).body("group deleted");
    }
    @PostMapping(path = "/addMember")
    ResponseEntity<String> addMembers(@RequestBody AddMembersRequest addMembersRequest){
        try{
            groupService.addMembersRequestFunction(addMembersRequest);
            return ResponseEntity.status(HttpStatus.OK).body("User added to group.");
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //to be changed later to limit info exposed
    @GetMapping(path = "/{groupId}/members")
    ResponseEntity<List<User>> getMembers(@PathVariable Integer groupId){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupMembers(groupId));
        }
        catch(NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping(path = "/name/{groupName}")
    ResponseEntity<GroupDTO> getGroupbyName(@PathVariable String groupName){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupInfoFromName(groupName));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping(path = "/all/names")
    ResponseEntity<List<String>> getAllGroupNames(){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(groupService.getGroupNames(groupRepository.findAll()));
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping(path = "/{groupId}/posts")
    ResponseEntity<List<PostDTO>> getAllPostsInGroup(@PathVariable Integer groupId) {
        Optional<Group> g = groupRepository.findByGroupId(groupId);
        if (g.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        List<PostDTO> posts = new ArrayList<>();
        for (Post p : g.get().getPosts()) {
            posts.add(PostService.convertPostToPostDTO(p));
        }

        List<PostDTO> toRet = PostService.sortPostDTOsByDate(posts);
        return new ResponseEntity<>(toRet, HttpStatus.OK);
    }

    @DeleteMapping(path = "/leave/{groupId}/{userId}")
    ResponseEntity leaveGroup(@PathVariable Integer groupId, @PathVariable Integer userId) {
        Optional<Group> g = groupRepository.findByGroupId(groupId);
        Optional<User> u = userRepository.findByUserId(userId);
        if (g.isEmpty() || u.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        Group group = g.get();
        if (group.getGroupOwner().getUserId() == userId) return new ResponseEntity("Owner cannot leave group, group must be deleted", HttpStatus.BAD_REQUEST);

        User user = u.get();
        List<User> currMembers = group.getMembers();
        if (currMembers.contains(user)) {
            currMembers.remove(user);
        }
        group.setMembers(currMembers);

        List<Group> groups = user.getMemberGroups();
        if (groups.contains(group)) {
            groups.remove(groups);
        }
        user.setMemberGroups(groups);

        userRepository.save(user);
        groupRepository.save(group);

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @GetMapping(path = "/all/{userId}")
    ResponseEntity<List<GroupDTO>> getAllGroupsFromUserId(@PathVariable Integer userId) {
        var user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        List<GroupDTO> groups = new ArrayList<>();
        for (Group g : user.get().getOwnedGroups()) groups.add(GroupService.groupToGroupDTO(g));
        for (Group g : user.get().getMemberGroups()) groups.add(GroupService.groupToGroupDTO(g));

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(path = "/all/owned/{userId}")
    ResponseEntity<List<GroupDTO>> getAllOwnedGroupsFromUserId(@PathVariable Integer userId) {
        var user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        List<GroupDTO> groups = new ArrayList<>();
        for (Group g : user.get().getOwnedGroups()) groups.add(GroupService.groupToGroupDTO(g));

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }

    @GetMapping(path = "/all/member/{userId}")
    ResponseEntity<List<GroupDTO>> getAllMemberGroupsFromUserId(@PathVariable Integer userId) {
        var user = userRepository.findByUserId(userId);
        if (user.isEmpty()) return new ResponseEntity(HttpStatus.BAD_REQUEST);

        List<GroupDTO> groups = new ArrayList<>();
        for (Group g : user.get().getMemberGroups()) groups.add(GroupService.groupToGroupDTO(g));

        return new ResponseEntity<>(groups, HttpStatus.OK);
    }
}
