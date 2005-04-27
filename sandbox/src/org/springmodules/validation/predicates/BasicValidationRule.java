package org.springmodules.validation.predicates;

import java.util.ArrayList;import java.util.Collection;import java.util.Iterator;import org.apache.commons.collections.Predicate;import org.springframework.beans.BeanWrapper;import org.springframework.beans.BeanWrapperImpl;import org.springframework.util.StringUtils;import org.springframework.validation.Errors;import org.springmodules.validation.functions.Function;


/**
 * <p>Validation rule implementation that will validate a target
 * bean an return an error message is the validation fails.
 * 
 * @author Steven Devijver
 * @since 23-04-2005
 */
public class BasicValidationRule implements ValidationRule {

	private Predicate predicate = null;
	private String field = null;
	private String errorMessage = null;
	private String errorKey = null;	private Collection errorArgs = null;	
	public BasicValidationRule(String field, Predicate predicate, String errorMessage) {
		super();
		setField(field);
		setPredicate(predicate);
		setErrorMessage(errorMessage);
	}	/*	 * JIRA-MOD-20: added error key and error args to validation rule, kudos to C�sar Ordi�ana.	 */	public BasicValidationRule(String field, Predicate predicate, String errorKey, String errorMessage, Collection errorArgs) {		this(field, predicate, errorMessage);		setErrorKey(errorKey);		setErrorArgs(errorArgs);	}

	private void setPredicate(Predicate predicate) {
		if (predicate == null) {
			throw new IllegalArgumentException("Predicate parameter must not be null!");
		}
		this.predicate = predicate;
	}
	
	private Predicate getPredicate() {
		return this.predicate;
	}

	private void setErrorMessage(String errorMessage) {
		if (errorMessage == null) {
			throw new IllegalArgumentException("Error message parameter must not be null!");
		}
		this.errorMessage = errorMessage;
	}

	private String getErrorMessage() {
		return this.errorMessage;
	}

	private void setField(String field) {
		if (field == null) {
			throw new IllegalArgumentException("Field parameter must not be null!");
		}
		this.field = field;
	}
	
	private String getField() {
		return this.field;
	}
	private void setErrorKey(String errorKey) {		this.errorKey = errorKey;	}		private String getErrorKey() {		return this.errorKey;	}	private void setErrorArgs(Collection errorArgs) {		this.errorArgs = errorArgs;	}		private Collection getErrorArgs() {		return this.errorArgs;	}	
	public void validate(Object target, Errors errors) {
		BeanWrapper beanWrapper = null;
		if (target instanceof BeanWrapper) {
			beanWrapper = (BeanWrapper)target;
		} else {
			beanWrapper = new BeanWrapperImpl(target);
		}
		if (!getPredicate().evaluate(beanWrapper)) {			/*			 * JIRA-MOD-20: take into account error key and error args for localization, kudos to C�sar Ordi�ana.			 */			if (StringUtils.hasLength(getErrorKey())) {				if (getErrorArgs() != null && !getErrorArgs().isEmpty()) {					Collection tmpColl = new ArrayList();					for (Iterator iter = getErrorArgs().iterator(); iter.hasNext();) {						tmpColl.add(((Function)iter.next()).getResult(target));					}					errors.rejectValue(getField(), getErrorKey(), tmpColl.toArray(), getErrorMessage());				} else {					errors.rejectValue(getField(), getErrorKey(), getErrorMessage());				}			} else {				errors.rejectValue(getField(), getField(), getErrorMessage());			}
			
		}
	}
}
