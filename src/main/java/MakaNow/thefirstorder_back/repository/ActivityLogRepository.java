package MakaNow.thefirstorder_back.repository;

import MakaNow.thefirstorder_back.model.ActivityLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityLogRepository extends CrudRepository<ActivityLog, String> {
}
