package aiss.videominer.repository;

import aiss.videominer.model.Channel;
import aiss.videominer.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChannelRepository extends JpaRepository<Channel,String> {
    @Query("""
    SELECT channel
    FROM Channel channel
    WHERE SIZE(channel.videos) >= :count
""")
    Page<Channel> findByVideoCountGreaterThan(
            @Param("count") int count,
            Pageable pageable
    );

    @Query("""
    SELECT channel
    FROM Channel channel
    WHERE SIZE(channel.videos) >= :count
""")
    List<Channel> findByVideoCountGreaterThanNoPagination(
            @Param("count") int count
    );
}
