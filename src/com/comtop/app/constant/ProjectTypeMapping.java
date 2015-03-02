package com.comtop.app.constant;

import java.util.HashMap;
import java.util.Map;

public class ProjectTypeMapping {

	private Map<Integer, String> projectType;

	public ProjectTypeMapping() {
		this.projectType = new HashMap<Integer, String>();
		projectType.put(1, "主网项目");
		projectType.put(2, "配网项目");
	}

	public String getProjectType(Integer key) {
		return projectType.get(key);
	}

}
