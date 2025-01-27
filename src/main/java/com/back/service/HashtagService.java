package com.back.service;

import com.back.domain.Hashtag;
import com.back.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;
    private final HashtagExtractor hashtagExtractor;

    public Set<Hashtag> extractAndSaveHashtags(String content) {
        // 해시태그 추출
        Set<String> hashtagNamesInContent = hashtagExtractor.extractHashtagNamesFromContent(content);

        if (hashtagNamesInContent.isEmpty()) {
            return new HashSet<>();
        }

        // DB에 있는 해시태그 조회
        Set<Hashtag> existingHashtags = hashtagRepository.findByHashtagNameIn(hashtagNamesInContent);

        // DB에 저장해야 할 해시태그
        Set<String> exitingHashtagNames = existingHashtags.stream()
                .map(Hashtag::getHashtagName)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        // DB에 없는 새로운 해시태그
        Set<Hashtag> newHashtags = hashtagNamesInContent.stream()
                .filter(hashtagName -> !exitingHashtagNames.contains(hashtagName))
                .map(Hashtag::newHashtag)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (!newHashtags.isEmpty()) {
            hashtagRepository.saveAll(newHashtags); // 새로운 해시태그 저장
            existingHashtags.addAll(newHashtags);
        }

        return Set.copyOf(existingHashtags);
    }

}
