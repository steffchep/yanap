package com.topdesk.yanap.web;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Created by stephaniep on 02.06.2016.
 */
@Getter
@EqualsAndHashCode
@ToString
class StatusUpdateData {
	long userId;
	int dayIndex;
	float value;
}

