package com.jstudyplanner.service.implementation;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jstudyplanner.dao.TermDAO;
import com.jstudyplanner.domain.Term;
import com.jstudyplanner.service.TermService;

@Component("termService")
public class TermServiceImpl implements TermService {
	
	@Autowired TermDAO termDAO;

	public List<Term> getAllTerms() {
		return termDAO.getAllTerms();
	}

	public List<Term> getTermsByStatus(boolean enabled) {
		return termDAO.getTermsByStatus(enabled);
	}

	public List<Term> getFutureTerms() {
		// TODO implement correct logic for getFutureTerms()
		return termDAO.getTermsByStatus(true);
	}

	/**
	 * Attempts to save/update term.
	 * Check if combination of year and number is unique. If not throw exception.
	 * Also check if start date is later then finish date. If start date is later than finish date - throw exception.
	 * Else save/update this term.
	 * If term doesn't have id then this is a new one - persist it. Else update existing term. 
	 */
	public void save(Term term) {
		// TODO business checks for courseAvailability and enrollments 
		Term termToCheck = termDAO.getTermByYearAndNumber(term.getYear(), term.getNumber());
		if (GeneralService.domainObjectsAreSame(term, termToCheck)) {
			throw new CustomServiceException("","There is already a term with year " + term.getYear()
				+ " and number " + term.getNumber() + ". Combination of term and number should be unique.");
		}
		
		if (term.getStartDate().compareTo(term.getFinishDate()) > 0) {
			throw new CustomServiceException("","Term start date should not be later than finish date.");
		}
		// TODO check start/finish dates against year, e.g. start date 01-01-2012 is not valid for year 2015
		
		if ( term.getId() == null ) {
			termDAO.add(term);
		} else {
			termDAO.save(term);
		}
	}

	public void delete(Term term) {
		// TODO throw exception if not found or unsuccessful. CustomServiceException
		// TODO check all business constraints for term delete (e.g. course availability, students enrolled)
		termDAO.delete(term);
	}

	public Term getTermById(Long id) {
		if (id != null) {
			return termDAO.getTermById(id);
		} else {
			throw new CustomServiceException("","Term is not defined.");
		}
	}

	public Term getTermByYearAndNumber(Integer year, Integer number) {
		return termDAO.getTermByYearAndNumber(year, number);
	}
}