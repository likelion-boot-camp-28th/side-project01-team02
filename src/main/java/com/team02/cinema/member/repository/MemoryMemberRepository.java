package com.team02.cinema.member.repository;

import com.team02.cinema.member.domain.Member;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryMemberRepository implements MemberRepository{
    private Map<String, Member> members = new HashMap<>();
    private long lastId = 0;

    @Override
    public void save(Member member){
        lastId++;
        member.assignId(lastId);
        members.put(member.getLoginId(), member);
    }

    public Optional<Member> findByLoginId(String loginId){
        return Optional.ofNullable(members.get(loginId));
    }
}
