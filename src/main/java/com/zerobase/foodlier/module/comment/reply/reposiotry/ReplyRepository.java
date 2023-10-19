package com.zerobase.foodlier.module.comment.reply.reposiotry;

import com.zerobase.foodlier.module.comment.reply.domain.model.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplySearchRepository {
}
