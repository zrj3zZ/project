package com.iwork.app.schedule;

public interface IWorkScheduleInterface {
	/**
	 * 任务将要被执行时触发
	 * @return 若返回false，不继续执行executeOn，并置任务执行状态为‘终止
	 * @throws ScheduleException 抛出异常后，该任务执行状态被标记为‘异常’
	 */
	public boolean executeBefore()throws ScheduleException;
	
	/**
	 * 任务执行时被触发
	 * @return 若返回false，不继续执行executeAfter，并置任务执行状态为‘终止’
	 * @throws ScheduleException 抛出异常后，该任务执行状态被标记为‘异常’
	 */
	public boolean executeOn()throws ScheduleException;
	
	/**
	 * 任务执行完后被触发
	 * @throws ScheduleException 抛出异常后，该任务执行状态被标记为‘异常’
	 */
	public boolean executeAfter()throws ScheduleException;
	
}
