package com.careerpath.domain.repository;

import com.careerpath.domain.modelOld.Bookmark;
import com.careerpath.domain.modelOld.ids.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {}
