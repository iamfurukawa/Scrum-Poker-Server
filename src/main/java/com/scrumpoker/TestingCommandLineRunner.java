package com.scrumpoker;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.scrumpoker.commons.DeckType;
import com.scrumpoker.commons.IssueType;
import com.scrumpoker.commons.SessionType;
import com.scrumpoker.commons.StatusType;
import com.scrumpoker.entity.Issue;
import com.scrumpoker.entity.Session;
import com.scrumpoker.entity.User;
import com.scrumpoker.entity.Vote;
import com.scrumpoker.repository.IssueRepository;
import com.scrumpoker.repository.SessionRepository;
import com.scrumpoker.repository.UserRepository;
import com.scrumpoker.repository.VoteRepository;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@SuppressWarnings("all")
public class TestingCommandLineRunner implements CommandLineRunner {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private SessionRepository sessionRepository;

	@Autowired
	private IssueRepository issueRepository;

	@Autowired
	private VoteRepository voteRepository;
	
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	private Session sessionOne;
	private Session sessionTwo;
	private Session sessionThree;
	private User userOne;
	private User userTwo;
	private User userThree;
	
	
	@Override
	public void run(String... args) throws Exception {
		
		User user = createUser("1");
		userOne = userRepository.save(user);
		log.info("User created: {}", userOne.getId());
		
		user = createUser("2");
		userTwo = userRepository.save(user);
		log.info("User created: {}", userTwo.getId());
		
		user = createUser("3");
		userThree = userRepository.save(user);
		log.info("User created: {}", userThree.getId());
		
		Session session = createSession("1");
		sessionOne = sessionRepository.save(session);
		log.info("Session created: {}", sessionOne.getId());
		
		session = createSession("2");
		sessionTwo = sessionRepository.save(session);
		log.info("Session created: {}", sessionTwo.getId());
		
		session = createSession("3");
		sessionThree = sessionRepository.save(session);
		log.info("Session created: {}", sessionThree.getId());
		
		create();
	}
	
	private void create() {
		
		//configure issues
		Issue issue = createIssue("1");
		issue.setPoints(2);
		Issue issue1 = issueRepository.save(issue);
		log.info("Issue created 1: {}", issue1.getId());
		
		issue = createIssue("2");
		issue.setPoints(4);
		Issue issue2 = issueRepository.save(issue);
		log.info("Issue created 2: {}", issue2.getId());
		
		issue = createIssue("3");
		issue.setPoints(6);
		Issue issue3 = issueRepository.save(issue);
		log.info("Issue created 3: {}", issue3.getId());
		
		issue = createIssue("4");
		issue.setPoints(9);
		Issue issue4 = issueRepository.save(issue);
		log.info("Issue created 4: {}", issue4.getId());
		
		issue = createIssue("5");
		issue.setPoints(15);
		Issue issue5 = issueRepository.save(issue);
		log.info("Issue created 5: {}", issue5.getId());
		
		issue = createIssue("6");
		issue.setPoints(24);
		Issue issue6 = issueRepository.save(issue);
		log.info("Issue created 6: {}", issue6.getId());
		
		//configure sessions
		sessionOne.setOwner(userOne);
		sessionOne.setParticipants(Arrays.asList(userOne, userTwo, userThree));
		sessionOne.setIssues(Arrays.asList(issue1));
		sessionOne = sessionRepository.save(sessionOne);
		log.info("Session updated 1");
		
		sessionTwo.setOwner(userTwo);
		sessionTwo.setParticipants(Arrays.asList(userTwo, userThree));
		sessionTwo.setIssues(Arrays.asList(issue2, issue3));
		sessionTwo = sessionRepository.save(sessionTwo);
		log.info("Session updated 2");
		
		sessionThree.setOwner(userTwo);
		sessionThree.setParticipants(Arrays.asList(userThree));
		sessionThree.setIssues(Arrays.asList(issue4, issue5, issue6));
		sessionThree.setStatus(StatusType.DEACTIVATED);
		sessionThree = sessionRepository.save(sessionThree);
		log.info("Session updated 3");
		
		//configure votes
		
		//issue 1
		Vote vote = createVote(userOne, issue1);
		Vote vote1 = voteRepository.save(vote);
		log.info("Vote created: {}", vote1.getId());
		
		vote = createVote(userTwo, issue1);
		Vote vote2 = voteRepository.save(vote);
		log.info("Vote created: {}", vote2.getId());
		
		vote = createVote(userThree, issue1);
		Vote vote3 = voteRepository.save(vote);
		log.info("Vote created: {}", vote3.getId());
		
		//issue 2
		vote = createVote(userTwo, issue2);
		Vote vote4 = voteRepository.save(vote);
		log.info("Vote created: {}", vote4.getId());
		
		vote = createVote(userTwo, issue3);
		Vote vote5 = voteRepository.save(vote);
		log.info("Vote created: {}", vote5.getId());
		
		vote = createVote(userThree, issue2);
		Vote vote6 = voteRepository.save(vote);
		log.info("Vote created: {}", vote6.getId());
		
		vote = createVote(userThree, issue3);
		Vote vote7 = voteRepository.save(vote);
		log.info("Vote created: {}", vote7.getId());
		
		//issue 3
		vote = createVote(userThree, issue4);
		Vote vote8 = voteRepository.save(vote);
		log.info("Vote created: {}", vote8.getId());
		
		vote = createVote(userThree, issue5);
		Vote vote9 = voteRepository.save(vote);
		log.info("Vote created: {}", vote9.getId());
		
		vote = createVote(userThree, issue6);
		Vote vote10 = voteRepository.save(vote);
		log.info("Vote created: {}", vote10.getId());
		
		//save set votes
		issue1.setVotes(Arrays.asList(vote1, vote2, vote3));
		issueRepository.save(issue1);
		
		issue2.setVotes(Arrays.asList(vote4, vote6));
		issueRepository.save(issue2);
		
		issue3.setVotes(Arrays.asList(vote5, vote7));
		issueRepository.save(issue3);
		
		issue4.setVotes(Arrays.asList(vote8));
		issueRepository.save(issue4);
		
		issue5.setVotes(Arrays.asList(vote9));
		issueRepository.save(issue5);
		
		issue6.setVotes(Arrays.asList(vote10));
		issueRepository.save(issue6);
	}

	private Vote createVote(final User userSaved, final Issue issueSaved) {
		return Vote.builder()
				.user(userSaved)
				.issue(issueSaved)
				.points(50)
				.build();
	}

	private Issue createIssue(final String id) {
		return Issue.builder()
				.link("URL"+id)
				.name("Issue " + id + "!")
				.points(0)
				.status(IssueType.IN_VOTE)
				.build();
	}

	private Session createSession(final String id) {
		return Session.builder()
				.deckType(DeckType.FIBONACCI)
				.maxParticipants(150)
				.name("Session " + id + "!")
				.password("PASSW")
				.sessionType(SessionType.PUBLIC)
				.owner(null)
				.status(StatusType.ACTIVE)
				.url("http://link")
				.build();
	}

	private User createUser(final String id) {
		return User.builder()
				.email("email" + id + "@email")
				.firstName("Vinícius "+id)
				.lastName("Furukawa "+id)
				.password(passwordEncoder.encode("senha"))
				.status(StatusType.ACTIVE)
				.token("asd1452sad")
				.validationToken(LocalDateTime.now().plusDays(2)).build();
	}
}
