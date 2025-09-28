package com.careerpath.repository;

import com.careerpath.model.Bookmark;
import com.careerpath.model.ids.BookmarkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<Bookmark, BookmarkId> {}
