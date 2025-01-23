package program.Groups.Service;
import program.Groups.Group;
import program.Groups.dto.*;
import program.Posts.Post;
import program.Posts.Repositories.PostRepository;
import program.Users.User;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import program.Users.UserRepository;
import program.Groups.GroupRepository;


@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    @Autowired
    public GroupService(GroupRepository groupRepository, UserRepository userRepository, PostRepository postRepository){
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    public List<Group> getGroupsForUser(User user){
        return groupRepository.findAllByMembers(user);
    }

    public void createGroupRequest(GroupCreationRequest groupCreationRequest){
        if(userRepository.existsByUserId(groupCreationRequest.getCreatorId())){
            if(groupRepository.existsByGroupName(groupCreationRequest.getGroupName())){
                throw new IllegalArgumentException("The Group Name exists already");
            }
            else{
                //Group creator exists and the name is not a duplicate
                createGroup(groupCreationRequest);
            }
        }
        else{
            throw new IllegalArgumentException("The Creator is not a real user (You messed up)");
        }

    }
    private void createGroup(GroupCreationRequest groupCreationRequest){
        Group group = new Group();
        User creator = userRepository.findByUserId(groupCreationRequest.getCreatorId()).get();
        group.setGroupName(groupCreationRequest.getGroupName());
        group.setGroupDescription(groupCreationRequest.getGroupDescription());
        group.setGroupOwner(creator);
        group.setGroupType(groupCreationRequest.getGroupType());
        group.getMembers().add(creator);
        groupRepository.save(group);
    }

    public void addMembersRequestFunction(AddMembersRequest addMembersRequest){
        if(!userRepository.existsByUserId(addMembersRequest.getUserId())){
            throw new IllegalArgumentException("The user is not a real user");
        }
        if(!groupRepository.existsByGroupId(addMembersRequest.getGroupId())){
            throw new IllegalArgumentException("The group is not a real group");
        }
        if(groupRepository.isUserInGroup(addMembersRequest.getGroupId(), addMembersRequest.getUserId())){
            throw new IllegalArgumentException("The user is already in this group");
        }
        else{
            Group group = groupRepository.findByGroupId(addMembersRequest.getGroupId()).get();
            User user = userRepository.findByUserId(addMembersRequest.getUserId()).get();
            group.getMembers().add(user);
            groupRepository.save(group);
        }
    }

//    public void addPostRequestFunction(AddPostRequest addPostRequest) {
//        // Make sure post exists
//        if(!postRepository.existsById(addPostRequest.getPostId())){
//            throw new IllegalArgumentException("The post is not a real post");
//        }
//        if(!groupRepository.existsByGroupId(addPostRequest.getGroupId())){
//            throw new IllegalArgumentException("The group is not a real group");
//        }
//        // Helper method returns true if post IS in group
//        if(! isPostInGroup(addPostRequest.getGroupId(), addPostRequest.getPostId())) {
//            // Now both post and group exist, and post is not in group
//            Group g = groupRepository.findByGroupId(addPostRequest.getGroupId()).get();
//            Post p = postRepository.findByPostId(addPostRequest.getPostId()).get();
//            g.addPostToGroup(p);
//            groupRepository.save(g);
//        }
//    }
    public static GroupDTO groupToGroupDTO(Group group){
        List<Integer> userIds = new ArrayList<Integer>();
        for (User user : group.getMembers()) {
            userIds.add(user.getUserId());
        }
        List<Integer> postIds = new ArrayList<Integer>();
        for (Post post : group.getPosts()) {
            postIds.add(post.getPostId());
        }
        return new GroupDTO(group.getGroupId(), group.getGroupName(), group.getGroupDescription(), group.getGroupType(), userIds, postIds);
    }
    public boolean isPostInGroup(Integer groupId, Integer postId) {
        // Trust
        Group g = groupRepository.findByGroupId(groupId).get();
        Post p = postRepository.findByPostId(postId).get();

        return g.getPosts().contains(p);
    }
    //needs to be updated
    public List<User> getGroupMembers(Integer groupId){
        return groupRepository.findByGroupId(groupId).get().getMembers();
    }
    
    public GroupDTO getGroupbyID(int groupId){
        Group group = groupRepository.findById(groupId)
            .orElseThrow(() -> new IllegalArgumentException("Group not found"));
        
        List<Integer> userIds = new ArrayList<Integer>();
        for (User user : group.getMembers()) {
            userIds.add(user.getUserId());
        }
        List<Integer> postIds = new ArrayList<Integer>();
        for (Post post : group.getPosts()) {
            postIds.add(post.getPostId());
        }
        return new GroupDTO(group.getGroupId(), group.getGroupName(), group.getGroupDescription(), group.getGroupType(), userIds, postIds);
    }

    public List<GroupDTO> getGroups(List<Group> list){
        List<GroupDTO> listDTO = new ArrayList<>();
        for (Group group : list) {
            listDTO.add(getGroupbyID(group.getGroupId()));
        }
        return listDTO;
    }
    public List<String> getGroupNames(List<Group> groups){
        List<String> listNames = new ArrayList<>();
        for (Group group : groups) {
            listNames.add(group.getGroupName());

            
        }
        return listNames;
    }
    public GroupDTO getGroupInfoFromName(String name){
        Group group = groupRepository.findByGroupName(name).get();
        return getGroupbyID(group.getGroupId());
    }
    public GroupDTO updateGroupRequest(UpdateGroupsRequest updateGroupRequest){
        Group group = groupRepository.findByGroupId(updateGroupRequest.getGroupId()).get();
        if ( updateGroupRequest.getGroupName() != null ) group.setGroupName(updateGroupRequest.getGroupName());
        if ( updateGroupRequest.getGroupDescription() != null )  group.setGroupDescription(updateGroupRequest.getGroupDescription());
        if ( updateGroupRequest.getGroupType() != null )  group.setGroupType(updateGroupRequest.getGroupType());
        groupRepository.save(group);
        return getGroupbyID(group.getGroupId());
    }
}
