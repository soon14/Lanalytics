package com.ai.analy.system.session;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

@SuppressWarnings("deprecation")
public class CacheHttpSession implements HttpSession, Serializable {
	private static final long serialVersionUID = 1L;
	protected long creationTime = 0L;
	protected String id;
	protected int maxInactiveInterval;
	protected long lastAccessedTime = 0L;
	protected transient boolean expired = false;
	protected transient boolean isNew = false;
	protected transient boolean isDirty = false;
	private transient SessionListener listener;
	private Map<String, Object> data = new ConcurrentHashMap<String, Object>();

	public void setListener(SessionListener listener) {
		this.listener = listener;
	}

	public long getCreationTime() {
		return this.creationTime;
	}

	public String getId() {
		return this.id;
	}

	public long getLastAccessedTime() {
		return this.lastAccessedTime;
	}

	public void setLastAccessedTime(long lastAccessedTime) {
		this.lastAccessedTime = lastAccessedTime;
	}

	public ServletContext getServletContext() {
		return null;
	}

	public void setMaxInactiveInterval(int i) {
		this.maxInactiveInterval = i;
	}

	public int getMaxInactiveInterval() {
		return this.maxInactiveInterval;
	}

	public Object getAttribute(String key) {
		return this.data.get(key);
	}

	public Object getValue(String key) {
		return this.data.get(key);
	}

	public Enumeration<String> getAttributeNames() {
		final Iterator<String> iterator = this.data.keySet().iterator();
		return new Enumeration<String>() {
			public boolean hasMoreElements() {
				return iterator.hasNext();
			}

			public String nextElement() {
				return iterator.next();
			}
		};
	}

	public String[] getValueNames() {
		String[] names = new String[this.data.size()];
		return (String[]) this.data.keySet().toArray(names);
	}

	public void setAttribute(String s, Object o) {
		if(s==null || o==null)
			return;
		this.data.put(s, o);
		this.isDirty = true;
	}

	public void putValue(String s, Object o) {
		if(s==null || o==null)
			return;
		this.data.put(s, o);
		this.isDirty = true;
	}

	public void removeAttribute(String s) {
		this.data.remove(s);
		this.isDirty = true;
	}

	public void removeValue(String s) {
		this.data.remove(s);
		this.isDirty = true;
	}

	public void invalidate() {
		this.expired = true;
		this.isDirty = true;
		if (this.listener != null)
			this.listener.onInvalidated(this);
	}

	public boolean isNew() {
		return this.isNew;
	}

	@Override
	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}
}
