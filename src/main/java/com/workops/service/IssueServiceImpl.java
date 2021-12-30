package com.workops.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.workops.dao.ComponentDao;
import com.workops.dao.IssueDao;
import com.workops.exception.ErrorDetails;
import com.workops.model.Component;
import com.workops.model.Issue;

@Service
@Transactional
public class IssueServiceImpl implements IssueService {

	@Autowired
	IssueDao idao;
	@Autowired
	ComponentDao cdao;
	@Override
	public List<Issue> getAllIssues() {
		return idao.findAllIssues();
	}

	@Override
	public Optional<Issue> getIssueById(String issueid) throws Exception {
		
		try
		{
		Optional<Issue> issue=idao.findById(issueid);
		if(!issue.isPresent())
		{
			throw  new ErrorDetails("Not Found Component With Given Id");
		}
		return issue;
		}
		catch(Exception e)
		{
			throw new ErrorDetails(e.getMessage());
		}
	}

	@Override
	public Issue createIssue(Issue issue) throws Exception {
		try{
		Optional<Issue> issuepresent=idao.findByName(issue.getName());
		if(!issuepresent.isPresent())
		{
			issue.setId(UUID.randomUUID().toString().substring(0,32));
//			Issue iss=new Issue();
//			iss.setId(UUID.randomUUID().toString().substring(0,32));
//			iss.getComponents().addAll(iss.getComponents().stream()
//					.map(v->{Component cc=cdao.findById(v.getId()).get();
//					 cc.getIssues().add(iss);
//					 return cc;
//					}).collect(Collectors.toList()));
			return idao.save(issue);
		}
		throw new ErrorDetails("Component Already Exists");
		}
		catch(Exception e)
		{
			throw new ErrorDetails("Error in Creating Issue="+e.getMessage());
		}
	}

	@Override
	public String updateIssue(Issue issue) {
		idao.delete(issue);
		idao.save(issue);
		return "Updated Issue";
	}

	@Override
	public void deleteIssueById(String Issueid) throws Exception {

		try
		{
		Optional<Issue> issue=idao.findById(Issueid);
		if(!issue.isPresent())
		{

			throw new ErrorDetails("No Issue Exists With this Id");
		}
		idao.deleteById(Issueid);
		}
		catch(Exception e)
		{
			throw new ErrorDetails("Error in Deleteing Issue");
		}
	}

	@Override
	public List<Issue> getAllIssuesBySprintId(String sprintid) {
		return idao.findAllIssuesBySprintId(sprintid);
	}

}
