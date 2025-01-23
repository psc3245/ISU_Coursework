package program.Groups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;
import program.Users.*;

public interface GroupRepository extends  JpaRepository<Group,Integer>{
    Optional<Group> findByGroupId(Integer id);
    Optional<Group> findByGroupName(String name);
    Optional<Group> findByGroupOwner(User user);
    boolean existsByGroupName(String name);
    boolean existsByGroupId(Integer id);
    @Query("SELECT CASE WHEN COUNT(g) > 0 THEN true ELSE false END " +
       "FROM Group g JOIN g.members m WHERE g.groupId = :groupId AND m.userId = :userId")
    boolean isUserInGroup(@Param("groupId") int groupId, @Param("userId") int userId);

    List<Group> findAllByGroupNameContainingIgnoreCase(String groupName);
    


    @Transactional
    void deleteByGroupId(Integer id);
    List<Group> findAllByMembers(User user);
}
