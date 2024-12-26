package sube.interviews.taskprocessor.util;

public class TaskConstants {

	public static final String TRANSITION_SEND_TO_MAIL = "sendToMail";
	public static final String TRANSITION_IN_TRAVEL = "inTravel";
	public static final String TRANSITION_DELIVERED = "delivered";
	public static final String TRANSITION_CANCELLED = "cancelled";
	
	public static final String TASK_STATUS_SUCCESS = "SUCCESS";
	public static final String TASK_STATUS_ERROR = "ERROR";
	public static final String TASK_STATUS_CONFLICT = "CONFLICT";
	public static final String MESSAGE_TASK_SUCCESS = "Task processed successfully, moved to state: ";
	public static final String MESSAGE_TASK_CONFLICT = "Concurrency conflict detected.";
	public static final String MESSAGE_TASK_FAILED = "Task failed: ";

}
