/*! jQuery v1.7.2 jquery.com | jquery.org/license */
(function(a, b) {
	function cy(a) {
		return f.isWindow(a) ? a: a.nodeType === 9 ? a.defaultView || a.parentWindow: !1
	}
	function cu(a) {
		if (!cj[a]) {
			var b = c.body,
			d = f("<" + a + ">").appendTo(b),
			e = d.css("display");
			d.remove();
			if (e === "none" || e === "") {
				ck || (ck = c.createElement("iframe"), ck.frameBorder = ck.width = ck.height = 0),
				b.appendChild(ck);
				if (!cl || !ck.createElement) {
					cl = (ck.contentWindow || ck.contentDocument).document,
					cl.write((f.support.boxModel ? "<!doctype html>": "") + "<html><body>"),
					cl.close()
				}
				d = cl.createElement(a),
				cl.body.appendChild(d),
				e = f.css(d, "display"),
				b.removeChild(ck)
			}
			cj[a] = e
		}
		return cj[a]
	}
	function ct(a, b) {
		var c = {};
		f.each(cp.concat.apply([], cp.slice(0, b)),
		function() {
			c[this] = a
		});
		return c
	}
	function cs() {
		cq = b
	}
	function cr() {
		setTimeout(cs, 0);
		return cq = f.now()
	}
	function ci() {
		try {
			return new a.ActiveXObject("Microsoft.XMLHTTP")
		} catch(b) {}
	}
	function ch() {
		try {
			return new a.XMLHttpRequest
		} catch(b) {}
	}
	function cb(a, c) {
		a.dataFilter && (c = a.dataFilter(c, a.dataType));
		var d = a.dataTypes,
		e = {},
		g, h, i = d.length,
		j, k = d[0],
		l,
		m,
		n,
		o,
		p;
		for (g = 1; g < i; g++) {
			if (g === 1) {
				for (h in a.converters) {
					typeof h == "string" && (e[h.toLowerCase()] = a.converters[h])
				}
			}
			l = k,
			k = d[g];
			if (k === "*") {
				k = l
			} else {
				if (l !== "*" && l !== k) {
					m = l + " " + k,
					n = e[m] || e["* " + k];
					if (!n) {
						p = b;
						for (o in e) {
							j = o.split(" ");
							if (j[0] === l || j[0] === "*") {
								p = e[j[1] + " " + k];
								if (p) {
									o = e[o],
									o === !0 ? n = p: p === !0 && (n = o);
									break
								}
							}
						}
					} ! n && !p && f.error("No conversion from " + m.replace(" ", " to ")),
					n !== !0 && (c = n ? n(c) : p(o(c)))
				}
			}
		}
		return c
	}
	function ca(a, c, d) {
		var e = a.contents,
		f = a.dataTypes,
		g = a.responseFields,
		h, i, j, k;
		for (i in g) {
			i in d && (c[g[i]] = d[i])
		}
		while (f[0] === "*") {
			f.shift(),
			h === b && (h = a.mimeType || c.getResponseHeader("content-type"))
		}
		if (h) {
			for (i in e) {
				if (e[i] && e[i].test(h)) {
					f.unshift(i);
					break
				}
			}
		}
		if (f[0] in d) {
			j = f[0]
		} else {
			for (i in d) {
				if (!f[0] || a.converters[i + " " + f[0]]) {
					j = i;
					break
				}
				k || (k = i)
			}
			j = j || k
		}
		if (j) {
			j !== f[0] && f.unshift(j);
			return d[j]
		}
	}
	function b_(a, b, c, d) {
		if (f.isArray(b)) {
			f.each(b,
			function(b, e) {
				c || bD.test(a) ? d(a, e) : b_(a + "[" + (typeof e == "object" ? b: "") + "]", e, c, d)
			})
		} else {
			if (!c && f.type(b) === "object") {
				for (var e in b) {
					b_(a + "[" + e + "]", b[e], c, d)
				}
			} else {
				d(a, b)
			}
		}
	}
	function b$(a, c) {
		var d, e, g = f.ajaxSettings.flatOptions || {};
		for (d in c) {
			c[d] !== b && ((g[d] ? a: e || (e = {}))[d] = c[d])
		}
		e && f.extend(!0, a, e)
	}
	function bZ(a, c, d, e, f, g) {
		f = f || c.dataTypes[0],
		g = g || {},
		g[f] = !0;
		var h = a[f],
		i = 0,
		j = h ? h.length: 0,
		k = a === bS,
		l;
		for (; i < j && (k || !l); i++) {
			l = h[i](c, d, e),
			typeof l == "string" && (!k || g[l] ? l = b: (c.dataTypes.unshift(l), l = bZ(a, c, d, e, l, g)))
		} (k || !l) && !g["*"] && (l = bZ(a, c, d, e, "*", g));
		return l
	}
	function bY(a) {
		return function(b, c) {
			typeof b != "string" && (c = b, b = "*");
			if (f.isFunction(c)) {
				var d = b.toLowerCase().split(bO),
				e = 0,
				g = d.length,
				h,
				i,
				j;
				for (; e < g; e++) {
					h = d[e],
					j = /^\+/.test(h),
					j && (h = h.substr(1) || "*"),
					i = a[h] = a[h] || [],
					i[j ? "unshift": "push"](c)
				}
			}
		}
	}
	function bB(a, b, c) {
		var d = b === "width" ? a.offsetWidth: a.offsetHeight,
		e = b === "width" ? 1 : 0,
		g = 4;
		if (d > 0) {
			if (c !== "border") {
				for (; e < g; e += 2) {
					c || (d -= parseFloat(f.css(a, "padding" + bx[e])) || 0),
					c === "margin" ? d += parseFloat(f.css(a, c + bx[e])) || 0 : d -= parseFloat(f.css(a, "border" + bx[e] + "Width")) || 0
				}
			}
			return d + "px"
		}
		d = by(a, b);
		if (d < 0 || d == null) {
			d = a.style[b]
		}
		if (bt.test(d)) {
			return d
		}
		d = parseFloat(d) || 0;
		if (c) {
			for (; e < g; e += 2) {
				d += parseFloat(f.css(a, "padding" + bx[e])) || 0,
				c !== "padding" && (d += parseFloat(f.css(a, "border" + bx[e] + "Width")) || 0),
				c === "margin" && (d += parseFloat(f.css(a, c + bx[e])) || 0)
			}
		}
		return d + "px"
	}
	function bo(a) {
		var b = c.createElement("div");
		bh.appendChild(b),
		b.innerHTML = a.outerHTML;
		return b.firstChild
	}
	function bn(a) {
		var b = (a.nodeName || "").toLowerCase();
		b === "input" ? bm(a) : b !== "script" && typeof a.getElementsByTagName != "undefined" && f.grep(a.getElementsByTagName("input"), bm)
	}
	function bm(a) {
		if (a.type === "checkbox" || a.type === "radio") {
			a.defaultChecked = a.checked
		}
	}
	function bl(a) {
		return typeof a.getElementsByTagName != "undefined" ? a.getElementsByTagName("*") : typeof a.querySelectorAll != "undefined" ? a.querySelectorAll("*") : []
	}
	function bk(a, b) {
		var c;
		b.nodeType === 1 && (b.clearAttributes && b.clearAttributes(), b.mergeAttributes && b.mergeAttributes(a), c = b.nodeName.toLowerCase(), c === "object" ? b.outerHTML = a.outerHTML: c !== "input" || a.type !== "checkbox" && a.type !== "radio" ? c === "option" ? b.selected = a.defaultSelected: c === "input" || c === "textarea" ? b.defaultValue = a.defaultValue: c === "script" && b.text !== a.text && (b.text = a.text) : (a.checked && (b.defaultChecked = b.checked = a.checked), b.value !== a.value && (b.value = a.value)), b.removeAttribute(f.expando), b.removeAttribute("_submit_attached"), b.removeAttribute("_change_attached"))
	}
	function bj(a, b) {
		if (b.nodeType === 1 && !!f.hasData(a)) {
			var c, d, e, g = f._data(a),
			h = f._data(b, g),
			i = g.events;
			if (i) {
				delete h.handle,
				h.events = {};
				for (c in i) {
					for (d = 0, e = i[c].length; d < e; d++) {
						f.event.add(b, c, i[c][d])
					}
				}
			}
			h.data && (h.data = f.extend({},
			h.data))
		}
	}
	function bi(a, b) {
		return f.nodeName(a, "table") ? a.getElementsByTagName("tbody")[0] || a.appendChild(a.ownerDocument.createElement("tbody")) : a
	}
	function U(a) {
		var b = V.split("|"),
		c = a.createDocumentFragment();
		if (c.createElement) {
			while (b.length) {
				c.createElement(b.pop())
			}
		}
		return c
	}
	function T(a, b, c) {
		b = b || 0;
		if (f.isFunction(b)) {
			return f.grep(a,
			function(a, d) {
				var e = !!b.call(a, d, a);
				return e === c
			})
		}
		if (b.nodeType) {
			return f.grep(a,
			function(a, d) {
				return a === b === c
			})
		}
		if (typeof b == "string") {
			var d = f.grep(a,
			function(a) {
				return a.nodeType === 1
			});
			if (O.test(b)) {
				return f.filter(b, d, !c)
			}
			b = f.filter(b, d)
		}
		return f.grep(a,
		function(a, d) {
			return f.inArray(a, b) >= 0 === c
		})
	}
	function S(a) {
		return ! a || !a.parentNode || a.parentNode.nodeType === 11
	}
	function K() {
		return ! 0
	}
	function J() {
		return ! 1
	}
	function n(a, b, c) {
		var d = b + "defer",
		e = b + "queue",
		g = b + "mark",
		h = f._data(a, d);
		h && (c === "queue" || !f._data(a, e)) && (c === "mark" || !f._data(a, g)) && setTimeout(function() { ! f._data(a, e) && !f._data(a, g) && (f.removeData(a, d, !0), h.fire())
		},
		0)
	}
	function m(a) {
		for (var b in a) {
			if (b === "data" && f.isEmptyObject(a[b])) {
				continue
			}
			if (b !== "toJSON") {
				return ! 1
			}
		}
		return ! 0
	}
	function l(a, c, d) {
		if (d === b && a.nodeType === 1) {
			var e = "data-" + c.replace(k, "-$1").toLowerCase();
			d = a.getAttribute(e);
			if (typeof d == "string") {
				try {
					d = d === "true" ? !0 : d === "false" ? !1 : d === "null" ? null: f.isNumeric(d) ? +d: j.test(d) ? f.parseJSON(d) : d
				} catch(g) {}
				f.data(a, c, d)
			} else {
				d = b
			}
		}
		return d
	}
	function h(a) {
		var b = g[a] = {},
		c,
		d;
		a = a.split(/\s+/);
		for (c = 0, d = a.length; c < d; c++) {
			b[a[c]] = !0
		}
		return b
	}
	var c = a.document,
	d = a.navigator,
	e = a.location,
	f = function() {
		function J() {
			if (!e.isReady) {
				try {
					c.documentElement.doScroll("left")
				} catch(a) {
					setTimeout(J, 1);
					return
				}
				e.ready()
			}
		}
		var e = function(a, b) {
			return new e.fn.init(a, b, h)
		},
		f = a.jQuery,
		g = a.$,
		h,
		i = /^(?:[^#<]*(<[\w\W]+>)[^>]*$|#([\w\-]*)$)/,
		j = /\S/,
		k = /^\s+/,
		l = /\s+$/,
		m = /^<(\w+)\s*\/?>(?:<\/\1>)?$/,
		n = /^[\],:{}\s]*$/,
		o = /\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g,
		p = /"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g,
		q = /(?:^|:|,)(?:\s*\[)+/g,
		r = /(webkit)[ \/]([\w.]+)/,
		s = /(opera)(?:.*version)?[ \/]([\w.]+)/,
		t = /(msie) ([\w.]+)/,
		u = /(mozilla)(?:.*? rv:([\w.]+))?/,
		v = /-([a-z]|[0-9])/ig,
		w = /^-ms-/,
		x = function(a, b) {
			return (b + "").toUpperCase()
		},
		y = d.userAgent,
		z,
		A,
		B,
		C = Object.prototype.toString,
		D = Object.prototype.hasOwnProperty,
		E = Array.prototype.push,
		F = Array.prototype.slice,
		G = String.prototype.trim,
		H = Array.prototype.indexOf,
		I = {};
		e.fn = e.prototype = {
			constructor: e,
			init: function(a, d, f) {
				var g, h, j, k;
				if (!a) {
					return this
				}
				if (a.nodeType) {
					this.context = this[0] = a,
					this.length = 1;
					return this
				}
				if (a === "body" && !d && c.body) {
					this.context = c,
					this[0] = c.body,
					this.selector = a,
					this.length = 1;
					return this
				}
				if (typeof a == "string") {
					a.charAt(0) !== "<" || a.charAt(a.length - 1) !== ">" || a.length < 3 ? g = i.exec(a) : g = [null, a, null];
					if (g && (g[1] || !d)) {
						if (g[1]) {
							d = d instanceof e ? d[0] : d,
							k = d ? d.ownerDocument || d: c,
							j = m.exec(a),
							j ? e.isPlainObject(d) ? (a = [c.createElement(j[1])], e.fn.attr.call(a, d, !0)) : a = [k.createElement(j[1])] : (j = e.buildFragment([g[1]], [k]), a = (j.cacheable ? e.clone(j.fragment) : j.fragment).childNodes);
							return e.merge(this, a)
						}
						h = c.getElementById(g[2]);
						if (h && h.parentNode) {
							if (h.id !== g[2]) {
								return f.find(a)
							}
							this.length = 1,
							this[0] = h
						}
						this.context = c,
						this.selector = a;
						return this
					}
					return ! d || d.jquery ? (d || f).find(a) : this.constructor(d).find(a)
				}
				if (e.isFunction(a)) {
					return f.ready(a)
				}
				a.selector !== b && (this.selector = a.selector, this.context = a.context);
				return e.makeArray(a, this)
			},
			selector: "",
			jquery: "1.7.2",
			length: 0,
			size: function() {
				return this.length
			},
			toArray: function() {
				return F.call(this, 0)
			},
			get: function(a) {
				return a == null ? this.toArray() : a < 0 ? this[this.length + a] : this[a]
			},
			pushStack: function(a, b, c) {
				var d = this.constructor();
				e.isArray(a) ? E.apply(d, a) : e.merge(d, a),
				d.prevObject = this,
				d.context = this.context,
				b === "find" ? d.selector = this.selector + (this.selector ? " ": "") + c: b && (d.selector = this.selector + "." + b + "(" + c + ")");
				return d
			},
			each: function(a, b) {
				return e.each(this, a, b)
			},
			ready: function(a) {
				e.bindReady(),
				A.add(a);
				return this
			},
			eq: function(a) {
				a = +a;
				return a === -1 ? this.slice(a) : this.slice(a, a + 1)
			},
			first: function() {
				return this.eq(0)
			},
			last: function() {
				return this.eq( - 1)
			},
			slice: function() {
				return this.pushStack(F.apply(this, arguments), "slice", F.call(arguments).join(","))
			},
			map: function(a) {
				return this.pushStack(e.map(this,
				function(b, c) {
					return a.call(b, c, b)
				}))
			},
			end: function() {
				return this.prevObject || this.constructor(null)
			},
			push: E,
			sort: [].sort,
			splice: [].splice
		},
		e.fn.init.prototype = e.fn,
		e.extend = e.fn.extend = function() {
			var a, c, d, f, g, h, i = arguments[0] || {},
			j = 1,
			k = arguments.length,
			l = !1;
			typeof i == "boolean" && (l = i, i = arguments[1] || {},
			j = 2),
			typeof i != "object" && !e.isFunction(i) && (i = {}),
			k === j && (i = this, --j);
			for (; j < k; j++) {
				if ((a = arguments[j]) != null) {
					for (c in a) {
						d = i[c],
						f = a[c];
						if (i === f) {
							continue
						}
						l && f && (e.isPlainObject(f) || (g = e.isArray(f))) ? (g ? (g = !1, h = d && e.isArray(d) ? d: []) : h = d && e.isPlainObject(d) ? d: {},
						i[c] = e.extend(l, h, f)) : f !== b && (i[c] = f)
					}
				}
			}
			return i
		},
		e.extend({
			noConflict: function(b) {
				a.$ === e && (a.$ = g),
				b && a.jQuery === e && (a.jQuery = f);
				return e
			},
			isReady: !1,
			readyWait: 1,
			holdReady: function(a) {
				a ? e.readyWait++:e.ready(!0)
			},
			ready: function(a) {
				if (a === !0 && !--e.readyWait || a !== !0 && !e.isReady) {
					if (!c.body) {
						return setTimeout(e.ready, 1)
					}
					e.isReady = !0;
					if (a !== !0 && --e.readyWait > 0) {
						return
					}
					A.fireWith(c, [e]),
					e.fn.trigger && e(c).trigger("ready").off("ready")
				}
			},
			bindReady: function() {
				if (!A) {
					A = e.Callbacks("once memory");
					if (c.readyState === "complete") {
						return setTimeout(e.ready, 1)
					}
					if (c.addEventListener) {
						c.addEventListener("DOMContentLoaded", B, !1),
						a.addEventListener("load", e.ready, !1)
					} else {
						if (c.attachEvent) {
							c.attachEvent("onreadystatechange", B),
							a.attachEvent("onload", e.ready);
							var b = !1;
							try {
								b = a.frameElement == null
							} catch(d) {}
							c.documentElement.doScroll && b && J()
						}
					}
				}
			},
			isFunction: function(a) {
				return e.type(a) === "function"
			},
			isArray: Array.isArray ||
			function(a) {
				return e.type(a) === "array"
			},
			isWindow: function(a) {
				return a != null && a == a.window
			},
			isNumeric: function(a) {
				return ! isNaN(parseFloat(a)) && isFinite(a)
			},
			type: function(a) {
				return a == null ? String(a) : I[C.call(a)] || "object"
			},
			isPlainObject: function(a) {
				if (!a || e.type(a) !== "object" || a.nodeType || e.isWindow(a)) {
					return ! 1
				}
				try {
					if (a.constructor && !D.call(a, "constructor") && !D.call(a.constructor.prototype, "isPrototypeOf")) {
						return ! 1
					}
				} catch(c) {
					return ! 1
				}
				var d;
				for (d in a) {}
				return d === b || D.call(a, d)
			},
			isEmptyObject: function(a) {
				for (var b in a) {
					return ! 1
				}
				return ! 0
			},
			error: function(a) {
				throw new Error(a)
			},
			parseJSON: function(b) {
				if (typeof b != "string" || !b) {
					return null
				}
				b = e.trim(b);
				if (a.JSON && a.JSON.parse) {
					return a.JSON.parse(b)
				}
				if (n.test(b.replace(o, "@").replace(p, "]").replace(q, ""))) {
					return (new Function("return " + b))()
				}
				e.error("Invalid JSON: " + b)
			},
			parseXML: function(c) {
				if (typeof c != "string" || !c) {
					return null
				}
				var d, f;
				try {
					a.DOMParser ? (f = new DOMParser, d = f.parseFromString(c, "text/xml")) : (d = new ActiveXObject("Microsoft.XMLDOM"), d.async = "false", d.loadXML(c))
				} catch(g) {
					d = b
				} (!d || !d.documentElement || d.getElementsByTagName("parsererror").length) && e.error("Invalid XML: " + c);
				return d
			},
			noop: function() {},
			globalEval: function(b) {
				b && j.test(b) && (a.execScript ||
				function(b) {
					a.eval.call(a, b)
				})(b)
			},
			camelCase: function(a) {
				return a.replace(w, "ms-").replace(v, x)
			},
			nodeName: function(a, b) {
				return a.nodeName && a.nodeName.toUpperCase() === b.toUpperCase()
			},
			each: function(a, c, d) {
				var f, g = 0,
				h = a.length,
				i = h === b || e.isFunction(a);
				if (d) {
					if (i) {
						for (f in a) {
							if (c.apply(a[f], d) === !1) {
								break
							}
						}
					} else {
						for (; g < h;) {
							if (c.apply(a[g++], d) === !1) {
								break
							}
						}
					}
				} else {
					if (i) {
						for (f in a) {
							if (c.call(a[f], f, a[f]) === !1) {
								break
							}
						}
					} else {
						for (; g < h;) {
							if (c.call(a[g], g, a[g++]) === !1) {
								break
							}
						}
					}
				}
				return a
			},
			trim: G ?
			function(a) {
				return a == null ? "": G.call(a)
			}: function(a) {
				return a == null ? "": (a + "").replace(k, "").replace(l, "")
			},
			makeArray: function(a, b) {
				var c = b || [];
				if (a != null) {
					var d = e.type(a);
					a.length == null || d === "string" || d === "function" || d === "regexp" || e.isWindow(a) ? E.call(c, a) : e.merge(c, a)
				}
				return c
			},
			inArray: function(a, b, c) {
				var d;
				if (b) {
					if (H) {
						return H.call(b, a, c)
					}
					d = b.length,
					c = c ? c < 0 ? Math.max(0, d + c) : c: 0;
					for (; c < d; c++) {
						if (c in b && b[c] === a) {
							return c
						}
					}
				}
				return - 1
			},
			merge: function(a, c) {
				var d = a.length,
				e = 0;
				if (typeof c.length == "number") {
					for (var f = c.length; e < f; e++) {
						a[d++] = c[e]
					}
				} else {
					while (c[e] !== b) {
						a[d++] = c[e++]
					}
				}
				a.length = d;
				return a
			},
			grep: function(a, b, c) {
				var d = [],
				e;
				c = !!c;
				for (var f = 0,
				g = a.length; f < g; f++) {
					e = !!b(a[f], f),
					c !== e && d.push(a[f])
				}
				return d
			},
			map: function(a, c, d) {
				var f, g, h = [],
				i = 0,
				j = a.length,
				k = a instanceof e || j !== b && typeof j == "number" && (j > 0 && a[0] && a[j - 1] || j === 0 || e.isArray(a));
				if (k) {
					for (; i < j; i++) {
						f = c(a[i], i, d),
						f != null && (h[h.length] = f)
					}
				} else {
					for (g in a) {
						f = c(a[g], g, d),
						f != null && (h[h.length] = f)
					}
				}
				return h.concat.apply([], h)
			},
			guid: 1,
			proxy: function(a, c) {
				if (typeof c == "string") {
					var d = a[c];
					c = a,
					a = d
				}
				if (!e.isFunction(a)) {
					return b
				}
				var f = F.call(arguments, 2),
				g = function() {
					return a.apply(c, f.concat(F.call(arguments)))
				};
				g.guid = a.guid = a.guid || g.guid || e.guid++;
				return g
			},
			access: function(a, c, d, f, g, h, i) {
				var j, k = d == null,
				l = 0,
				m = a.length;
				if (d && typeof d == "object") {
					for (l in d) {
						e.access(a, c, l, d[l], 1, h, f)
					}
					g = 1
				} else {
					if (f !== b) {
						j = i === b && e.isFunction(f),
						k && (j ? (j = c, c = function(a, b, c) {
							return j.call(e(a), c)
						}) : (c.call(a, f), c = null));
						if (c) {
							for (; l < m; l++) {
								c(a[l], d, j ? f.call(a[l], l, c(a[l], d)) : f, i)
							}
						}
						g = 1
					}
				}
				return g ? a: k ? c.call(a) : m ? c(a[0], d) : h
			},
			now: function() {
				return (new Date).getTime()
			},
			uaMatch: function(a) {
				a = a.toLowerCase();
				var b = r.exec(a) || s.exec(a) || t.exec(a) || a.indexOf("compatible") < 0 && u.exec(a) || [];
				return {
					browser: b[1] || "",
					version: b[2] || "0"
				}
			},
			sub: function() {
				function a(b, c) {
					return new a.fn.init(b, c)
				}
				e.extend(!0, a, this),
				a.superclass = this,
				a.fn = a.prototype = this(),
				a.fn.constructor = a,
				a.sub = this.sub,
				a.fn.init = function(d, f) {
					f && f instanceof e && !(f instanceof a) && (f = a(f));
					return e.fn.init.call(this, d, f, b)
				},
				a.fn.init.prototype = a.fn;
				var b = a(c);
				return a
			},
			browser: {}
		}),
		e.each("Boolean Number String Function Array Date RegExp Object".split(" "),
		function(a, b) {
			I["[object " + b + "]"] = b.toLowerCase()
		}),
		z = e.uaMatch(y),
		z.browser && (e.browser[z.browser] = !0, e.browser.version = z.version),
		e.browser.webkit && (e.browser.safari = !0),
		j.test(" ") && (k = /^[\s\xA0]+/, l = /[\s\xA0]+$/),
		h = e(c),
		c.addEventListener ? B = function() {
			c.removeEventListener("DOMContentLoaded", B, !1),
			e.ready()
		}: c.attachEvent && (B = function() {
			c.readyState === "complete" && (c.detachEvent("onreadystatechange", B), e.ready())
		});
		return e
	} (),
	g = {};
	f.Callbacks = function(a) {
		a = a ? g[a] || h(a) : {};
		var c = [],
		d = [],
		e,
		i,
		j,
		k,
		l,
		m,
		n = function(b) {
			var d, e, g, h, i;
			for (d = 0, e = b.length; d < e; d++) {
				g = b[d],
				h = f.type(g),
				h === "array" ? n(g) : h === "function" && (!a.unique || !p.has(g)) && c.push(g)
			}
		},
		o = function(b, f) {
			f = f || [],
			e = !a.memory || [b, f],
			i = !0,
			j = !0,
			m = k || 0,
			k = 0,
			l = c.length;
			for (; c && m < l; m++) {
				if (c[m].apply(b, f) === !1 && a.stopOnFalse) {
					e = !0;
					break
				}
			}
			j = !1,
			c && (a.once ? e === !0 ? p.disable() : c = [] : d && d.length && (e = d.shift(), p.fireWith(e[0], e[1])))
		},
		p = {
			add: function() {
				if (c) {
					var a = c.length;
					n(arguments),
					j ? l = c.length: e && e !== !0 && (k = a, o(e[0], e[1]))
				}
				return this
			},
			remove: function() {
				if (c) {
					var b = arguments,
					d = 0,
					e = b.length;
					for (; d < e; d++) {
						for (var f = 0; f < c.length; f++) {
							if (b[d] === c[f]) {
								j && f <= l && (l--, f <= m && m--),
								c.splice(f--, 1);
								if (a.unique) {
									break
								}
							}
						}
					}
				}
				return this
			},
			has: function(a) {
				if (c) {
					var b = 0,
					d = c.length;
					for (; b < d; b++) {
						if (a === c[b]) {
							return ! 0
						}
					}
				}
				return ! 1
			},
			empty: function() {
				c = [];
				return this
			},
			disable: function() {
				c = d = e = b;
				return this
			},
			disabled: function() {
				return ! c
			},
			lock: function() {
				d = b,
				(!e || e === !0) && p.disable();
				return this
			},
			locked: function() {
				return ! d
			},
			fireWith: function(b, c) {
				d && (j ? a.once || d.push([b, c]) : (!a.once || !e) && o(b, c));
				return this
			},
			fire: function() {
				p.fireWith(this, arguments);
				return this
			},
			fired: function() {
				return !! i
			}
		};
		return p
	};
	var i = [].slice;
	f.extend({
		Deferred: function(a) {
			var b = f.Callbacks("once memory"),
			c = f.Callbacks("once memory"),
			d = f.Callbacks("memory"),
			e = "pending",
			g = {
				resolve: b,
				reject: c,
				notify: d
			},
			h = {
				done: b.add,
				fail: c.add,
				progress: d.add,
				state: function() {
					return e
				},
				isResolved: b.fired,
				isRejected: c.fired,
				then: function(a, b, c) {
					i.done(a).fail(b).progress(c);
					return this
				},
				always: function() {
					i.done.apply(i, arguments).fail.apply(i, arguments);
					return this
				},
				pipe: function(a, b, c) {
					return f.Deferred(function(d) {
						f.each({
							done: [a, "resolve"],
							fail: [b, "reject"],
							progress: [c, "notify"]
						},
						function(a, b) {
							var c = b[0],
							e = b[1],
							g;
							f.isFunction(c) ? i[a](function() {
								g = c.apply(this, arguments),
								g && f.isFunction(g.promise) ? g.promise().then(d.resolve, d.reject, d.notify) : d[e + "With"](this === i ? d: this, [g])
							}) : i[a](d[e])
						})
					}).promise()
				},
				promise: function(a) {
					if (a == null) {
						a = h
					} else {
						for (var b in h) {
							a[b] = h[b]
						}
					}
					return a
				}
			},
			i = h.promise({}),
			j;
			for (j in g) {
				i[j] = g[j].fire,
				i[j + "With"] = g[j].fireWith
			}
			i.done(function() {
				e = "resolved"
			},
			c.disable, d.lock).fail(function() {
				e = "rejected"
			},
			b.disable, d.lock),
			a && a.call(i, i);
			return i
		},
		when: function(a) {
			function m(a) {
				return function(b) {
					e[a] = arguments.length > 1 ? i.call(arguments, 0) : b,
					j.notifyWith(k, e)
				}
			}
			function l(a) {
				return function(c) {
					b[a] = arguments.length > 1 ? i.call(arguments, 0) : c,
					--g || j.resolveWith(j, b)
				}
			}
			var b = i.call(arguments, 0),
			c = 0,
			d = b.length,
			e = Array(d),
			g = d,
			h = d,
			j = d <= 1 && a && f.isFunction(a.promise) ? a: f.Deferred(),
			k = j.promise();
			if (d > 1) {
				for (; c < d; c++) {
					b[c] && b[c].promise && f.isFunction(b[c].promise) ? b[c].promise().then(l(c), j.reject, m(c)) : --g
				}
				g || j.resolveWith(j, b)
			} else {
				j !== a && j.resolveWith(j, d ? [a] : [])
			}
			return k
		}
	}),
	f.support = function() {
		var b, d, e, g, h, i, j, k, l, m, n, o, p = c.createElement("div"),
		q = c.documentElement;
		p.setAttribute("className", "t"),
		p.innerHTML = "   <link/><table></table><a href='/a' style='top:1px;float:left;opacity:.55;'>a</a><input type='checkbox'/>",
		d = p.getElementsByTagName("*"),
		e = p.getElementsByTagName("a")[0];
		if (!d || !d.length || !e) {
			return {}
		}
		g = c.createElement("select"),
		h = g.appendChild(c.createElement("option")),
		i = p.getElementsByTagName("input")[0],
		b = {
			leadingWhitespace: p.firstChild.nodeType === 3,
			tbody: !p.getElementsByTagName("tbody").length,
			htmlSerialize: !!p.getElementsByTagName("link").length,
			style: /top/.test(e.getAttribute("style")),
			hrefNormalized: e.getAttribute("href") === "/a",
			opacity: /^0.55/.test(e.style.opacity),
			cssFloat: !!e.style.cssFloat,
			checkOn: i.value === "on",
			optSelected: h.selected,
			getSetAttribute: p.className !== "t",
			enctype: !!c.createElement("form").enctype,
			html5Clone: c.createElement("nav").cloneNode(!0).outerHTML !== "<:nav></:nav>",
			submitBubbles: !0,
			changeBubbles: !0,
			focusinBubbles: !1,
			deleteExpando: !0,
			noCloneEvent: !0,
			inlineBlockNeedsLayout: !1,
			shrinkWrapBlocks: !1,
			reliableMarginRight: !0,
			pixelMargin: !0
		},
		f.boxModel = b.boxModel = c.compatMode === "CSS1Compat",
		i.checked = !0,
		b.noCloneChecked = i.cloneNode(!0).checked,
		g.disabled = !0,
		b.optDisabled = !h.disabled;
		try {
			delete p.test
		} catch(r) {
			b.deleteExpando = !1
		} ! p.addEventListener && p.attachEvent && p.fireEvent && (p.attachEvent("onclick",
		function() {
			b.noCloneEvent = !1
		}), p.cloneNode(!0).fireEvent("onclick")),
		i = c.createElement("input"),
		i.value = "t",
		i.setAttribute("type", "radio"),
		b.radioValue = i.value === "t",
		i.setAttribute("checked", "checked"),
		i.setAttribute("name", "t"),
		p.appendChild(i),
		j = c.createDocumentFragment(),
		j.appendChild(p.lastChild),
		b.checkClone = j.cloneNode(!0).cloneNode(!0).lastChild.checked,
		b.appendChecked = i.checked,
		j.removeChild(i),
		j.appendChild(p);
		if (p.attachEvent) {
			for (n in {
				submit: 1,
				change: 1,
				focusin: 1
			}) {
				m = "on" + n,
				o = m in p,
				o || (p.setAttribute(m, "return;"), o = typeof p[m] == "function"),
				b[n + "Bubbles"] = o
			}
		}
		j.removeChild(p),
		j = g = h = p = i = null,
		f(function() {
			var d, e, g, h, i, j, l, m, n, q, r, s, t, u = c.getElementsByTagName("body")[0]; ! u || (m = 1, t = "padding:0;margin:0;border:", r = "position:absolute;top:0;left:0;width:1px;height:1px;", s = t + "0;visibility:hidden;", n = "style='" + r + t + "5px solid #000;", q = "<div " + n + "display:block;'><div style='" + t + "0;display:block;overflow:hidden;'></div></div><table " + n + "' cellpadding='0' cellspacing='0'><tr><td></td></tr></table>", d = c.createElement("div"), d.style.cssText = s + "width:0;height:0;position:static;top:0;margin-top:" + m + "px", u.insertBefore(d, u.firstChild), p = c.createElement("div"), d.appendChild(p), p.innerHTML = "<table><tr><td style='" + t + "0;display:none'></td><td>t</td></tr></table>", k = p.getElementsByTagName("td"), o = k[0].offsetHeight === 0, k[0].style.display = "", k[1].style.display = "none", b.reliableHiddenOffsets = o && k[0].offsetHeight === 0, a.getComputedStyle && (p.innerHTML = "", l = c.createElement("div"), l.style.width = "0", l.style.marginRight = "0", p.style.width = "2px", p.appendChild(l), b.reliableMarginRight = (parseInt((a.getComputedStyle(l, null) || {
				marginRight: 0
			}).marginRight, 10) || 0) === 0), typeof p.style.zoom != "undefined" && (p.innerHTML = "", p.style.width = p.style.padding = "1px", p.style.border = 0, p.style.overflow = "hidden", p.style.display = "inline", p.style.zoom = 1, b.inlineBlockNeedsLayout = p.offsetWidth === 3, p.style.display = "block", p.style.overflow = "visible", p.innerHTML = "<div style='width:5px;'></div>", b.shrinkWrapBlocks = p.offsetWidth !== 3), p.style.cssText = r + s, p.innerHTML = q, e = p.firstChild, g = e.firstChild, i = e.nextSibling.firstChild.firstChild, j = {
				doesNotAddBorder: g.offsetTop !== 5,
				doesAddBorderForTableAndCells: i.offsetTop === 5
			},
			g.style.position = "fixed", g.style.top = "20px", j.fixedPosition = g.offsetTop === 20 || g.offsetTop === 15, g.style.position = g.style.top = "", e.style.overflow = "hidden", e.style.position = "relative", j.subtractsBorderForOverflowNotVisible = g.offsetTop === -5, j.doesNotIncludeMarginInBodyOffset = u.offsetTop !== m, a.getComputedStyle && (p.style.marginTop = "1%", b.pixelMargin = (a.getComputedStyle(p, null) || {
				marginTop: 0
			}).marginTop !== "1%"), typeof d.style.zoom != "undefined" && (d.style.zoom = 1), u.removeChild(d), l = p = d = null, f.extend(b, j))
		});
		return b
	} ();
	var j = /^(?:\{.*\}|\[.*\])$/,
	k = /([A-Z])/g;
	f.extend({
		cache: {},
		uuid: 0,
		expando: "jQuery" + (f.fn.jquery + Math.random()).replace(/\D/g, ""),
		noData: {
			embed: !0,
			object: "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000",
			applet: !0
		},
		hasData: function(a) {
			a = a.nodeType ? f.cache[a[f.expando]] : a[f.expando];
			return !! a && !m(a)
		},
		data: function(a, c, d, e) {
			if ( !! f.acceptData(a)) {
				var g, h, i, j = f.expando,
				k = typeof c == "string",
				l = a.nodeType,
				m = l ? f.cache: a,
				n = l ? a[j] : a[j] && j,
				o = c === "events";
				if ((!n || !m[n] || !o && !e && !m[n].data) && k && d === b) {
					return
				}
				n || (l ? a[j] = n = ++f.uuid: n = j),
				m[n] || (m[n] = {},
				l || (m[n].toJSON = f.noop));
				if (typeof c == "object" || typeof c == "function") {
					e ? m[n] = f.extend(m[n], c) : m[n].data = f.extend(m[n].data, c)
				}
				g = h = m[n],
				e || (h.data || (h.data = {}), h = h.data),
				d !== b && (h[f.camelCase(c)] = d);
				if (o && !h[c]) {
					return g.events
				}
				k ? (i = h[c], i == null && (i = h[f.camelCase(c)])) : i = h;
				return i
			}
		},
		removeData: function(a, b, c) {
			if ( !! f.acceptData(a)) {
				var d, e, g, h = f.expando,
				i = a.nodeType,
				j = i ? f.cache: a,
				k = i ? a[h] : h;
				if (!j[k]) {
					return
				}
				if (b) {
					d = c ? j[k] : j[k].data;
					if (d) {
						f.isArray(b) || (b in d ? b = [b] : (b = f.camelCase(b), b in d ? b = [b] : b = b.split(" ")));
						for (e = 0, g = b.length; e < g; e++) {
							delete d[b[e]]
						}
						if (! (c ? m: f.isEmptyObject)(d)) {
							return
						}
					}
				}
				if (!c) {
					delete j[k].data;
					if (!m(j[k])) {
						return
					}
				}
				f.support.deleteExpando || !j.setInterval ? delete j[k] : j[k] = null,
				i && (f.support.deleteExpando ? delete a[h] : a.removeAttribute ? a.removeAttribute(h) : a[h] = null)
			}
		},
		_data: function(a, b, c) {
			return f.data(a, b, c, !0)
		},
		acceptData: function(a) {
			if (a.nodeName) {
				var b = f.noData[a.nodeName.toLowerCase()];
				if (b) {
					return b !== !0 && a.getAttribute("classid") === b
				}
			}
			return ! 0
		}
	}),
	f.fn.extend({
		data: function(a, c) {
			var d, e, g, h, i, j = this[0],
			k = 0,
			m = null;
			if (a === b) {
				if (this.length) {
					m = f.data(j);
					if (j.nodeType === 1 && !f._data(j, "parsedAttrs")) {
						g = j.attributes;
						for (i = g.length; k < i; k++) {
							h = g[k].name,
							h.indexOf("data-") === 0 && (h = f.camelCase(h.substring(5)), l(j, h, m[h]))
						}
						f._data(j, "parsedAttrs", !0)
					}
				}
				return m
			}
			if (typeof a == "object") {
				return this.each(function() {
					f.data(this, a)
				})
			}
			d = a.split(".", 2),
			d[1] = d[1] ? "." + d[1] : "",
			e = d[1] + "!";
			return f.access(this,
			function(c) {
				if (c === b) {
					m = this.triggerHandler("getData" + e, [d[0]]),
					m === b && j && (m = f.data(j, a), m = l(j, a, m));
					return m === b && d[1] ? this.data(d[0]) : m
				}
				d[1] = c,
				this.each(function() {
					var b = f(this);
					b.triggerHandler("setData" + e, d),
					f.data(this, a, c),
					b.triggerHandler("changeData" + e, d)
				})
			},
			null, c, arguments.length > 1, null, !1)
		},
		removeData: function(a) {
			return this.each(function() {
				f.removeData(this, a)
			})
		}
	}),
	f.extend({
		_mark: function(a, b) {
			a && (b = (b || "fx") + "mark", f._data(a, b, (f._data(a, b) || 0) + 1))
		},
		_unmark: function(a, b, c) {
			a !== !0 && (c = b, b = a, a = !1);
			if (b) {
				c = c || "fx";
				var d = c + "mark",
				e = a ? 0 : (f._data(b, d) || 1) - 1;
				e ? f._data(b, d, e) : (f.removeData(b, d, !0), n(b, c, "mark"))
			}
		},
		queue: function(a, b, c) {
			var d;
			if (a) {
				b = (b || "fx") + "queue",
				d = f._data(a, b),
				c && (!d || f.isArray(c) ? d = f._data(a, b, f.makeArray(c)) : d.push(c));
				return d || []
			}
		},
		dequeue: function(a, b) {
			b = b || "fx";
			var c = f.queue(a, b),
			d = c.shift(),
			e = {};
			d === "inprogress" && (d = c.shift()),
			d && (b === "fx" && c.unshift("inprogress"), f._data(a, b + ".run", e), d.call(a,
			function() {
				f.dequeue(a, b)
			},
			e)),
			c.length || (f.removeData(a, b + "queue " + b + ".run", !0), n(a, b, "queue"))
		}
	}),
	f.fn.extend({
		queue: function(a, c) {
			var d = 2;
			typeof a != "string" && (c = a, a = "fx", d--);
			if (arguments.length < d) {
				return f.queue(this[0], a)
			}
			return c === b ? this: this.each(function() {
				var b = f.queue(this, a, c);
				a === "fx" && b[0] !== "inprogress" && f.dequeue(this, a)
			})
		},
		dequeue: function(a) {
			return this.each(function() {
				f.dequeue(this, a)
			})
		},
		delay: function(a, b) {
			a = f.fx ? f.fx.speeds[a] || a: a,
			b = b || "fx";
			return this.queue(b,
			function(b, c) {
				var d = setTimeout(b, a);
				c.stop = function() {
					clearTimeout(d)
				}
			})
		},
		clearQueue: function(a) {
			return this.queue(a || "fx", [])
		},
		promise: function(a, c) {
			function m() {--h || d.resolveWith(e, [e])
			}
			typeof a != "string" && (c = a, a = b),
			a = a || "fx";
			var d = f.Deferred(),
			e = this,
			g = e.length,
			h = 1,
			i = a + "defer",
			j = a + "queue",
			k = a + "mark",
			l;
			while (g--) {
				if (l = f.data(e[g], i, b, !0) || (f.data(e[g], j, b, !0) || f.data(e[g], k, b, !0)) && f.data(e[g], i, f.Callbacks("once memory"), !0)) {
					h++,
					l.add(m)
				}
			}
			m();
			return d.promise(c)
		}
	});
	var o = /[\n\t\r]/g,
	p = /\s+/,
	q = /\r/g,
	r = /^(?:button|input)$/i,
	s = /^(?:button|input|object|select|textarea)$/i,
	t = /^a(?:rea)?$/i,
	u = /^(?:autofocus|autoplay|async|checked|controls|defer|disabled|hidden|loop|multiple|open|readonly|required|scoped|selected)$/i,
	v = f.support.getSetAttribute,
	w, x, y;
	f.fn.extend({
		attr: function(a, b) {
			return f.access(this, f.attr, a, b, arguments.length > 1)
		},
		removeAttr: function(a) {
			return this.each(function() {
				f.removeAttr(this, a)
			})
		},
		prop: function(a, b) {
			return f.access(this, f.prop, a, b, arguments.length > 1)
		},
		removeProp: function(a) {
			a = f.propFix[a] || a;
			return this.each(function() {
				try {
					this[a] = b,
					delete this[a]
				} catch(c) {}
			})
		},
		addClass: function(a) {
			var b, c, d, e, g, h, i;
			if (f.isFunction(a)) {
				return this.each(function(b) {
					f(this).addClass(a.call(this, b, this.className))
				})
			}
			if (a && typeof a == "string") {
				b = a.split(p);
				for (c = 0, d = this.length; c < d; c++) {
					e = this[c];
					if (e.nodeType === 1) {
						if (!e.className && b.length === 1) {
							e.className = a
						} else {
							g = " " + e.className + " ";
							for (h = 0, i = b.length; h < i; h++) {~g.indexOf(" " + b[h] + " ") || (g += b[h] + " ")
							}
							e.className = f.trim(g)
						}
					}
				}
			}
			return this
		},
		removeClass: function(a) {
			var c, d, e, g, h, i, j;
			if (f.isFunction(a)) {
				return this.each(function(b) {
					f(this).removeClass(a.call(this, b, this.className))
				})
			}
			if (a && typeof a == "string" || a === b) {
				c = (a || "").split(p);
				for (d = 0, e = this.length; d < e; d++) {
					g = this[d];
					if (g.nodeType === 1 && g.className) {
						if (a) {
							h = (" " + g.className + " ").replace(o, " ");
							for (i = 0, j = c.length; i < j; i++) {
								h = h.replace(" " + c[i] + " ", " ")
							}
							g.className = f.trim(h)
						} else {
							g.className = ""
						}
					}
				}
			}
			return this
		},
		toggleClass: function(a, b) {
			var c = typeof a,
			d = typeof b == "boolean";
			if (f.isFunction(a)) {
				return this.each(function(c) {
					f(this).toggleClass(a.call(this, c, this.className, b), b)
				})
			}
			return this.each(function() {
				if (c === "string") {
					var e, g = 0,
					h = f(this),
					i = b,
					j = a.split(p);
					while (e = j[g++]) {
						i = d ? i: !h.hasClass(e),
						h[i ? "addClass": "removeClass"](e)
					}
				} else {
					if (c === "undefined" || c === "boolean") {
						this.className && f._data(this, "__className__", this.className),
						this.className = this.className || a === !1 ? "": f._data(this, "__className__") || ""
					}
				}
			})
		},
		hasClass: function(a) {
			var b = " " + a + " ",
			c = 0,
			d = this.length;
			for (; c < d; c++) {
				if (this[c].nodeType === 1 && (" " + this[c].className + " ").replace(o, " ").indexOf(b) > -1) {
					return ! 0
				}
			}
			return ! 1
		},
		val: function(a) {
			var c, d, e, g = this[0];
			if ( !! arguments.length) {
				e = f.isFunction(a);
				return this.each(function(d) {
					var g = f(this),
					h;
					if (this.nodeType === 1) {
						e ? h = a.call(this, d, g.val()) : h = a,
						h == null ? h = "": typeof h == "number" ? h += "": f.isArray(h) && (h = f.map(h,
						function(a) {
							return a == null ? "": a + ""
						})),
						c = f.valHooks[this.type] || f.valHooks[this.nodeName.toLowerCase()];
						if (!c || !("set" in c) || c.set(this, h, "value") === b) {
							this.value = h
						}
					}
				})
			}
			if (g) {
				c = f.valHooks[g.type] || f.valHooks[g.nodeName.toLowerCase()];
				if (c && "get" in c && (d = c.get(g, "value")) !== b) {
					return d
				}
				d = g.value;
				return typeof d == "string" ? d.replace(q, "") : d == null ? "": d
			}
		}
	}),
	f.extend({
		valHooks: {
			option: {
				get: function(a) {
					var b = a.attributes.value;
					return ! b || b.specified ? a.value: a.text
				}
			},
			select: {
				get: function(a) {
					var b, c, d, e, g = a.selectedIndex,
					h = [],
					i = a.options,
					j = a.type === "select-one";
					if (g < 0) {
						return null
					}
					c = j ? g: 0,
					d = j ? g + 1 : i.length;
					for (; c < d; c++) {
						e = i[c];
						if (e.selected && (f.support.optDisabled ? !e.disabled: e.getAttribute("disabled") === null) && (!e.parentNode.disabled || !f.nodeName(e.parentNode, "optgroup"))) {
							b = f(e).val();
							if (j) {
								return b
							}
							h.push(b)
						}
					}
					if (j && !h.length && i.length) {
						return f(i[g]).val()
					}
					return h
				},
				set: function(a, b) {
					var c = f.makeArray(b);
					f(a).find("option").each(function() {
						this.selected = f.inArray(f(this).val(), c) >= 0
					}),
					c.length || (a.selectedIndex = -1);
					return c
				}
			}
		},
		attrFn: {
			val: !0,
			css: !0,
			html: !0,
			text: !0,
			data: !0,
			width: !0,
			height: !0,
			offset: !0
		},
		attr: function(a, c, d, e) {
			var g, h, i, j = a.nodeType;
			if ( !! a && j !== 3 && j !== 8 && j !== 2) {
				if (e && c in f.attrFn) {
					return f(a)[c](d)
				}
				if (typeof a.getAttribute == "undefined") {
					return f.prop(a, c, d)
				}
				i = j !== 1 || !f.isXMLDoc(a),
				i && (c = c.toLowerCase(), h = f.attrHooks[c] || (u.test(c) ? x: w));
				if (d !== b) {
					if (d === null) {
						f.removeAttr(a, c);
						return
					}
					if (h && "set" in h && i && (g = h.set(a, d, c)) !== b) {
						return g
					}
					a.setAttribute(c, "" + d);
					return d
				}
				if (h && "get" in h && i && (g = h.get(a, c)) !== null) {
					return g
				}
				g = a.getAttribute(c);
				return g === null ? b: g
			}
		},
		removeAttr: function(a, b) {
			var c, d, e, g, h, i = 0;
			if (b && a.nodeType === 1) {
				d = b.toLowerCase().split(p),
				g = d.length;
				for (; i < g; i++) {
					e = d[i],
					e && (c = f.propFix[e] || e, h = u.test(e), h || f.attr(a, e, ""), a.removeAttribute(v ? e: c), h && c in a && (a[c] = !1))
				}
			}
		},
		attrHooks: {
			type: {
				set: function(a, b) {
					if (r.test(a.nodeName) && a.parentNode) {
						f.error("type property can't be changed")
					} else {
						if (!f.support.radioValue && b === "radio" && f.nodeName(a, "input")) {
							var c = a.value;
							a.setAttribute("type", b),
							c && (a.value = c);
							return b
						}
					}
				}
			},
			value: {
				get: function(a, b) {
					if (w && f.nodeName(a, "button")) {
						return w.get(a, b)
					}
					return b in a ? a.value: null
				},
				set: function(a, b, c) {
					if (w && f.nodeName(a, "button")) {
						return w.set(a, b, c)
					}
					a.value = b
				}
			}
		},
		propFix: {
			tabindex: "tabIndex",
			readonly: "readOnly",
			"for": "htmlFor",
			"class": "className",
			maxlength: "maxLength",
			cellspacing: "cellSpacing",
			cellpadding: "cellPadding",
			rowspan: "rowSpan",
			colspan: "colSpan",
			usemap: "useMap",
			frameborder: "frameBorder",
			contenteditable: "contentEditable"
		},
		prop: function(a, c, d) {
			var e, g, h, i = a.nodeType;
			if ( !! a && i !== 3 && i !== 8 && i !== 2) {
				h = i !== 1 || !f.isXMLDoc(a),
				h && (c = f.propFix[c] || c, g = f.propHooks[c]);
				return d !== b ? g && "set" in g && (e = g.set(a, d, c)) !== b ? e: a[c] = d: g && "get" in g && (e = g.get(a, c)) !== null ? e: a[c]
			}
		},
		propHooks: {
			tabIndex: {
				get: function(a) {
					var c = a.getAttributeNode("tabindex");
					return c && c.specified ? parseInt(c.value, 10) : s.test(a.nodeName) || t.test(a.nodeName) && a.href ? 0 : b
				}
			}
		}
	}),
	f.attrHooks.tabindex = f.propHooks.tabIndex,
	x = {
		get: function(a, c) {
			var d, e = f.prop(a, c);
			return e === !0 || typeof e != "boolean" && (d = a.getAttributeNode(c)) && d.nodeValue !== !1 ? c.toLowerCase() : b
		},
		set: function(a, b, c) {
			var d;
			b === !1 ? f.removeAttr(a, c) : (d = f.propFix[c] || c, d in a && (a[d] = !0), a.setAttribute(c, c.toLowerCase()));
			return c
		}
	},
	v || (y = {
		name: !0,
		id: !0,
		coords: !0
	},
	w = f.valHooks.button = {
		get: function(a, c) {
			var d;
			d = a.getAttributeNode(c);
			return d && (y[c] ? d.nodeValue !== "": d.specified) ? d.nodeValue: b
		},
		set: function(a, b, d) {
			var e = a.getAttributeNode(d);
			e || (e = c.createAttribute(d), a.setAttributeNode(e));
			return e.nodeValue = b + ""
		}
	},
	f.attrHooks.tabindex.set = w.set, f.each(["width", "height"],
	function(a, b) {
		f.attrHooks[b] = f.extend(f.attrHooks[b], {
			set: function(a, c) {
				if (c === "") {
					a.setAttribute(b, "auto");
					return c
				}
			}
		})
	}), f.attrHooks.contenteditable = {
		get: w.get,
		set: function(a, b, c) {
			b === "" && (b = "false"),
			w.set(a, b, c)
		}
	}),
	f.support.hrefNormalized || f.each(["href", "src", "width", "height"],
	function(a, c) {
		f.attrHooks[c] = f.extend(f.attrHooks[c], {
			get: function(a) {
				var d = a.getAttribute(c, 2);
				return d === null ? b: d
			}
		})
	}),
	f.support.style || (f.attrHooks.style = {
		get: function(a) {
			return a.style.cssText.toLowerCase() || b
		},
		set: function(a, b) {
			return a.style.cssText = "" + b
		}
	}),
	f.support.optSelected || (f.propHooks.selected = f.extend(f.propHooks.selected, {
		get: function(a) {
			var b = a.parentNode;
			b && (b.selectedIndex, b.parentNode && b.parentNode.selectedIndex);
			return null
		}
	})),
	f.support.enctype || (f.propFix.enctype = "encoding"),
	f.support.checkOn || f.each(["radio", "checkbox"],
	function() {
		f.valHooks[this] = {
			get: function(a) {
				return a.getAttribute("value") === null ? "on": a.value
			}
		}
	}),
	f.each(["radio", "checkbox"],
	function() {
		f.valHooks[this] = f.extend(f.valHooks[this], {
			set: function(a, b) {
				if (f.isArray(b)) {
					return a.checked = f.inArray(f(a).val(), b) >= 0
				}
			}
		})
	});
	var z = /^(?:textarea|input|select)$/i,
	A = /^([^\.]*)?(?:\.(.+))?$/,
	B = /(?:^|\s)hover(\.\S+)?\b/,
	C = /^key/,
	D = /^(?:mouse|contextmenu)|click/,
	E = /^(?:focusinfocus|focusoutblur)$/,
	F = /^(\w*)(?:#([\w\-]+))?(?:\.([\w\-]+))?$/,
	G = function(a) {
		var b = F.exec(a);
		b && (b[1] = (b[1] || "").toLowerCase(), b[3] = b[3] && new RegExp("(?:^|\\s)" + b[3] + "(?:\\s|$)"));
		return b
	},
	H = function(a, b) {
		var c = a.attributes || {};
		return (!b[1] || a.nodeName.toLowerCase() === b[1]) && (!b[2] || (c.id || {}).value === b[2]) && (!b[3] || b[3].test((c["class"] || {}).value))
	},
	I = function(a) {
		return f.event.special.hover ? a: a.replace(B, "mouseenter$1 mouseleave$1")
	};
	f.event = {
		add: function(a, c, d, e, g) {
			var h, i, j, k, l, m, n, o, p, q, r, s;
			if (! (a.nodeType === 3 || a.nodeType === 8 || !c || !d || !(h = f._data(a)))) {
				d.handler && (p = d, d = p.handler, g = p.selector),
				d.guid || (d.guid = f.guid++),
				j = h.events,
				j || (h.events = j = {}),
				i = h.handle,
				i || (h.handle = i = function(a) {
					return typeof f != "undefined" && (!a || f.event.triggered !== a.type) ? f.event.dispatch.apply(i.elem, arguments) : b
				},
				i.elem = a),
				c = f.trim(I(c)).split(" ");
				for (k = 0; k < c.length; k++) {
					l = A.exec(c[k]) || [],
					m = l[1],
					n = (l[2] || "").split(".").sort(),
					s = f.event.special[m] || {},
					m = (g ? s.delegateType: s.bindType) || m,
					s = f.event.special[m] || {},
					o = f.extend({
						type: m,
						origType: l[1],
						data: e,
						handler: d,
						guid: d.guid,
						selector: g,
						quick: g && G(g),
						namespace: n.join(".")
					},
					p),
					r = j[m];
					if (!r) {
						r = j[m] = [],
						r.delegateCount = 0;
						if (!s.setup || s.setup.call(a, e, n, i) === !1) {
							a.addEventListener ? a.addEventListener(m, i, !1) : a.attachEvent && a.attachEvent("on" + m, i)
						}
					}
					s.add && (s.add.call(a, o), o.handler.guid || (o.handler.guid = d.guid)),
					g ? r.splice(r.delegateCount++, 0, o) : r.push(o),
					f.event.global[m] = !0
				}
				a = null
			}
		},
		global: {},
		remove: function(a, b, c, d, e) {
			var g = f.hasData(a) && f._data(a),
			h,
			i,
			j,
			k,
			l,
			m,
			n,
			o,
			p,
			q,
			r,
			s;
			if ( !! g && !!(o = g.events)) {
				b = f.trim(I(b || "")).split(" ");
				for (h = 0; h < b.length; h++) {
					i = A.exec(b[h]) || [],
					j = k = i[1],
					l = i[2];
					if (!j) {
						for (j in o) {
							f.event.remove(a, j + b[h], c, d, !0)
						}
						continue
					}
					p = f.event.special[j] || {},
					j = (d ? p.delegateType: p.bindType) || j,
					r = o[j] || [],
					m = r.length,
					l = l ? new RegExp("(^|\\.)" + l.split(".").sort().join("\\.(?:.*\\.)?") + "(\\.|$)") : null;
					for (n = 0; n < r.length; n++) {
						s = r[n],
						(e || k === s.origType) && (!c || c.guid === s.guid) && (!l || l.test(s.namespace)) && (!d || d === s.selector || d === "**" && s.selector) && (r.splice(n--, 1), s.selector && r.delegateCount--, p.remove && p.remove.call(a, s))
					}
					r.length === 0 && m !== r.length && ((!p.teardown || p.teardown.call(a, l) === !1) && f.removeEvent(a, j, g.handle), delete o[j])
				}
				f.isEmptyObject(o) && (q = g.handle, q && (q.elem = null), f.removeData(a, ["events", "handle"], !0))
			}
		},
		customEvent: {
			getData: !0,
			setData: !0,
			changeData: !0
		},
		trigger: function(c, d, e, g) {
			if (!e || e.nodeType !== 3 && e.nodeType !== 8) {
				var h = c.type || c,
				i = [],
				j,
				k,
				l,
				m,
				n,
				o,
				p,
				q,
				r,
				s;
				if (E.test(h + f.event.triggered)) {
					return
				}
				h.indexOf("!") >= 0 && (h = h.slice(0, -1), k = !0),
				h.indexOf(".") >= 0 && (i = h.split("."), h = i.shift(), i.sort());
				if ((!e || f.event.customEvent[h]) && !f.event.global[h]) {
					return
				}
				c = typeof c == "object" ? c[f.expando] ? c: new f.Event(h, c) : new f.Event(h),
				c.type = h,
				c.isTrigger = !0,
				c.exclusive = k,
				c.namespace = i.join("."),
				c.namespace_re = c.namespace ? new RegExp("(^|\\.)" + i.join("\\.(?:.*\\.)?") + "(\\.|$)") : null,
				o = h.indexOf(":") < 0 ? "on" + h: "";
				if (!e) {
					j = f.cache;
					for (l in j) {
						j[l].events && j[l].events[h] && f.event.trigger(c, d, j[l].handle.elem, !0)
					}
					return
				}
				c.result = b,
				c.target || (c.target = e),
				d = d != null ? f.makeArray(d) : [],
				d.unshift(c),
				p = f.event.special[h] || {};
				if (p.trigger && p.trigger.apply(e, d) === !1) {
					return
				}
				r = [[e, p.bindType || h]];
				if (!g && !p.noBubble && !f.isWindow(e)) {
					s = p.delegateType || h,
					m = E.test(s + h) ? e: e.parentNode,
					n = null;
					for (; m; m = m.parentNode) {
						r.push([m, s]),
						n = m
					}
					n && n === e.ownerDocument && r.push([n.defaultView || n.parentWindow || a, s])
				}
				for (l = 0; l < r.length && !c.isPropagationStopped(); l++) {
					m = r[l][0],
					c.type = r[l][1],
					q = (f._data(m, "events") || {})[c.type] && f._data(m, "handle"),
					q && q.apply(m, d),
					q = o && m[o],
					q && f.acceptData(m) && q.apply(m, d) === !1 && c.preventDefault()
				}
				c.type = h,
				!g && !c.isDefaultPrevented() && (!p._default || p._default.apply(e.ownerDocument, d) === !1) && (h !== "click" || !f.nodeName(e, "a")) && f.acceptData(e) && o && e[h] && (h !== "focus" && h !== "blur" || c.target.offsetWidth !== 0) && !f.isWindow(e) && (n = e[o], n && (e[o] = null), f.event.triggered = h, e[h](), f.event.triggered = b, n && (e[o] = n));
				return c.result
			}
		},
		dispatch: function(c) {
			c = f.event.fix(c || a.event);
			var d = (f._data(this, "events") || {})[c.type] || [],
			e = d.delegateCount,
			g = [].slice.call(arguments, 0),
			h = !c.exclusive && !c.namespace,
			i = f.event.special[c.type] || {},
			j = [],
			k,
			l,
			m,
			n,
			o,
			p,
			q,
			r,
			s,
			t,
			u;
			g[0] = c,
			c.delegateTarget = this;
			if (!i.preDispatch || i.preDispatch.call(this, c) !== !1) {
				if (e && (!c.button || c.type !== "click")) {
					n = f(this),
					n.context = this.ownerDocument || this;
					for (m = c.target; m != this; m = m.parentNode || this) {
						if (m.disabled !== !0) {
							p = {},
							r = [],
							n[0] = m;
							for (k = 0; k < e; k++) {
								s = d[k],
								t = s.selector,
								p[t] === b && (p[t] = s.quick ? H(m, s.quick) : n.is(t)),
								p[t] && r.push(s)
							}
							r.length && j.push({
								elem: m,
								matches: r
							})
						}
					}
				}
				d.length > e && j.push({
					elem: this,
					matches: d.slice(e)
				});
				for (k = 0; k < j.length && !c.isPropagationStopped(); k++) {
					q = j[k],
					c.currentTarget = q.elem;
					for (l = 0; l < q.matches.length && !c.isImmediatePropagationStopped(); l++) {
						s = q.matches[l];
						if (h || !c.namespace && !s.namespace || c.namespace_re && c.namespace_re.test(s.namespace)) {
							c.data = s.data,
							c.handleObj = s,
							o = ((f.event.special[s.origType] || {}).handle || s.handler).apply(q.elem, g),
							o !== b && (c.result = o, o === !1 && (c.preventDefault(), c.stopPropagation()))
						}
					}
				}
				i.postDispatch && i.postDispatch.call(this, c);
				return c.result
			}
		},
		props: "attrChange attrName relatedNode srcElement altKey bubbles cancelable ctrlKey currentTarget eventPhase metaKey relatedTarget shiftKey target timeStamp view which".split(" "),
		fixHooks: {},
		keyHooks: {
			props: "char charCode key keyCode".split(" "),
			filter: function(a, b) {
				a.which == null && (a.which = b.charCode != null ? b.charCode: b.keyCode);
				return a
			}
		},
		mouseHooks: {
			props: "button buttons clientX clientY fromElement offsetX offsetY pageX pageY screenX screenY toElement".split(" "),
			filter: function(a, d) {
				var e, f, g, h = d.button,
				i = d.fromElement;
				a.pageX == null && d.clientX != null && (e = a.target.ownerDocument || c, f = e.documentElement, g = e.body, a.pageX = d.clientX + (f && f.scrollLeft || g && g.scrollLeft || 0) - (f && f.clientLeft || g && g.clientLeft || 0), a.pageY = d.clientY + (f && f.scrollTop || g && g.scrollTop || 0) - (f && f.clientTop || g && g.clientTop || 0)),
				!a.relatedTarget && i && (a.relatedTarget = i === a.target ? d.toElement: i),
				!a.which && h !== b && (a.which = h & 1 ? 1 : h & 2 ? 3 : h & 4 ? 2 : 0);
				return a
			}
		},
		fix: function(a) {
			if (a[f.expando]) {
				return a
			}
			var d, e, g = a,
			h = f.event.fixHooks[a.type] || {},
			i = h.props ? this.props.concat(h.props) : this.props;
			a = f.Event(g);
			for (d = i.length; d;) {
				e = i[--d],
				a[e] = g[e]
			}
			a.target || (a.target = g.srcElement || c),
			a.target.nodeType === 3 && (a.target = a.target.parentNode),
			a.metaKey === b && (a.metaKey = a.ctrlKey);
			return h.filter ? h.filter(a, g) : a
		},
		special: {
			ready: {
				setup: f.bindReady
			},
			load: {
				noBubble: !0
			},
			focus: {
				delegateType: "focusin"
			},
			blur: {
				delegateType: "focusout"
			},
			beforeunload: {
				setup: function(a, b, c) {
					f.isWindow(this) && (this.onbeforeunload = c)
				},
				teardown: function(a, b) {
					this.onbeforeunload === b && (this.onbeforeunload = null)
				}
			}
		},
		simulate: function(a, b, c, d) {
			var e = f.extend(new f.Event, c, {
				type: a,
				isSimulated: !0,
				originalEvent: {}
			});
			d ? f.event.trigger(e, null, b) : f.event.dispatch.call(b, e),
			e.isDefaultPrevented() && c.preventDefault()
		}
	},
	f.event.handle = f.event.dispatch,
	f.removeEvent = c.removeEventListener ?
	function(a, b, c) {
		a.removeEventListener && a.removeEventListener(b, c, !1)
	}: function(a, b, c) {
		a.detachEvent && a.detachEvent("on" + b, c)
	},
	f.Event = function(a, b) {
		if (! (this instanceof f.Event)) {
			return new f.Event(a, b)
		}
		a && a.type ? (this.originalEvent = a, this.type = a.type, this.isDefaultPrevented = a.defaultPrevented || a.returnValue === !1 || a.getPreventDefault && a.getPreventDefault() ? K: J) : this.type = a,
		b && f.extend(this, b),
		this.timeStamp = a && a.timeStamp || f.now(),
		this[f.expando] = !0
	},
	f.Event.prototype = {
		preventDefault: function() {
			this.isDefaultPrevented = K;
			var a = this.originalEvent; ! a || (a.preventDefault ? a.preventDefault() : a.returnValue = !1)
		},
		stopPropagation: function() {
			this.isPropagationStopped = K;
			var a = this.originalEvent; ! a || (a.stopPropagation && a.stopPropagation(), a.cancelBubble = !0)
		},
		stopImmediatePropagation: function() {
			this.isImmediatePropagationStopped = K,
			this.stopPropagation()
		},
		isDefaultPrevented: J,
		isPropagationStopped: J,
		isImmediatePropagationStopped: J
	},
	f.each({
		mouseenter: "mouseover",
		mouseleave: "mouseout"
	},
	function(a, b) {
		f.event.special[a] = {
			delegateType: b,
			bindType: b,
			handle: function(a) {
				var c = this,
				d = a.relatedTarget,
				e = a.handleObj,
				g = e.selector,
				h;
				if (!d || d !== c && !f.contains(c, d)) {
					a.type = e.origType,
					h = e.handler.apply(this, arguments),
					a.type = b
				}
				return h
			}
		}
	}),
	f.support.submitBubbles || (f.event.special.submit = {
		setup: function() {
			if (f.nodeName(this, "form")) {
				return ! 1
			}
			f.event.add(this, "click._submit keypress._submit",
			function(a) {
				var c = a.target,
				d = f.nodeName(c, "input") || f.nodeName(c, "button") ? c.form: b;
				d && !d._submit_attached && (f.event.add(d, "submit._submit",
				function(a) {
					a._submit_bubble = !0
				}), d._submit_attached = !0)
			})
		},
		postDispatch: function(a) {
			a._submit_bubble && (delete a._submit_bubble, this.parentNode && !a.isTrigger && f.event.simulate("submit", this.parentNode, a, !0))
		},
		teardown: function() {
			if (f.nodeName(this, "form")) {
				return ! 1
			}
			f.event.remove(this, "._submit")
		}
	}),
	f.support.changeBubbles || (f.event.special.change = {
		setup: function() {
			if (z.test(this.nodeName)) {
				if (this.type === "checkbox" || this.type === "radio") {
					f.event.add(this, "propertychange._change",
					function(a) {
						a.originalEvent.propertyName === "checked" && (this._just_changed = !0)
					}),
					f.event.add(this, "click._change",
					function(a) {
						this._just_changed && !a.isTrigger && (this._just_changed = !1, f.event.simulate("change", this, a, !0))
					})
				}
				return ! 1
			}
			f.event.add(this, "beforeactivate._change",
			function(a) {
				var b = a.target;
				z.test(b.nodeName) && !b._change_attached && (f.event.add(b, "change._change",
				function(a) {
					this.parentNode && !a.isSimulated && !a.isTrigger && f.event.simulate("change", this.parentNode, a, !0)
				}), b._change_attached = !0)
			})
		},
		handle: function(a) {
			var b = a.target;
			if (this !== b || a.isSimulated || a.isTrigger || b.type !== "radio" && b.type !== "checkbox") {
				return a.handleObj.handler.apply(this, arguments)
			}
		},
		teardown: function() {
			f.event.remove(this, "._change");
			return z.test(this.nodeName)
		}
	}),
	f.support.focusinBubbles || f.each({
		focus: "focusin",
		blur: "focusout"
	},
	function(a, b) {
		var d = 0,
		e = function(a) {
			f.event.simulate(b, a.target, f.event.fix(a), !0)
		};
		f.event.special[b] = {
			setup: function() {
				d++===0 && c.addEventListener(a, e, !0)
			},
			teardown: function() {--d === 0 && c.removeEventListener(a, e, !0)
			}
		}
	}),
	f.fn.extend({
		on: function(a, c, d, e, g) {
			var h, i;
			if (typeof a == "object") {
				typeof c != "string" && (d = d || c, c = b);
				for (i in a) {
					this.on(i, c, d, a[i], g)
				}
				return this
			}
			d == null && e == null ? (e = c, d = c = b) : e == null && (typeof c == "string" ? (e = d, d = b) : (e = d, d = c, c = b));
			if (e === !1) {
				e = J
			} else {
				if (!e) {
					return this
				}
			}
			g === 1 && (h = e, e = function(a) {
				f().off(a);
				return h.apply(this, arguments)
			},
			e.guid = h.guid || (h.guid = f.guid++));
			return this.each(function() {
				f.event.add(this, a, e, d, c)
			})
		},
		one: function(a, b, c, d) {
			return this.on(a, b, c, d, 1)
		},
		off: function(a, c, d) {
			if (a && a.preventDefault && a.handleObj) {
				var e = a.handleObj;
				f(a.delegateTarget).off(e.namespace ? e.origType + "." + e.namespace: e.origType, e.selector, e.handler);
				return this
			}
			if (typeof a == "object") {
				for (var g in a) {
					this.off(g, c, a[g])
				}
				return this
			}
			if (c === !1 || typeof c == "function") {
				d = c,
				c = b
			}
			d === !1 && (d = J);
			return this.each(function() {
				f.event.remove(this, a, d, c)
			})
		},
		bind: function(a, b, c) {
			return this.on(a, null, b, c)
		},
		unbind: function(a, b) {
			return this.off(a, null, b)
		},
		live: function(a, b, c) {
			f(this.context).on(a, this.selector, b, c);
			return this
		},
		die: function(a, b) {
			f(this.context).off(a, this.selector || "**", b);
			return this
		},
		delegate: function(a, b, c, d) {
			return this.on(b, a, c, d)
		},
		undelegate: function(a, b, c) {
			return arguments.length == 1 ? this.off(a, "**") : this.off(b, a, c)
		},
		trigger: function(a, b) {
			return this.each(function() {
				f.event.trigger(a, b, this)
			})
		},
		triggerHandler: function(a, b) {
			if (this[0]) {
				return f.event.trigger(a, b, this[0], !0)
			}
		},
		toggle: function(a) {
			var b = arguments,
			c = a.guid || f.guid++,
			d = 0,
			e = function(c) {
				var e = (f._data(this, "lastToggle" + a.guid) || 0) % d;
				f._data(this, "lastToggle" + a.guid, e + 1),
				c.preventDefault();
				return b[e].apply(this, arguments) || !1
			};
			e.guid = c;
			while (d < b.length) {
				b[d++].guid = c
			}
			return this.click(e)
		},
		hover: function(a, b) {
			return this.mouseenter(a).mouseleave(b || a)
		}
	}),
	f.each("blur focus focusin focusout load resize scroll unload click dblclick mousedown mouseup mousemove mouseover mouseout mouseenter mouseleave change select submit keydown keypress keyup error contextmenu".split(" "),
	function(a, b) {
		f.fn[b] = function(a, c) {
			c == null && (c = a, a = null);
			return arguments.length > 0 ? this.on(b, null, a, c) : this.trigger(b)
		},
		f.attrFn && (f.attrFn[b] = !0),
		C.test(b) && (f.event.fixHooks[b] = f.event.keyHooks),
		D.test(b) && (f.event.fixHooks[b] = f.event.mouseHooks)
	}),
	function() {
		function x(a, b, c, e, f, g) {
			for (var h = 0,
			i = e.length; h < i; h++) {
				var j = e[h];
				if (j) {
					var k = !1;
					j = j[a];
					while (j) {
						if (j[d] === c) {
							k = e[j.sizset];
							break
						}
						if (j.nodeType === 1) {
							g || (j[d] = c, j.sizset = h);
							if (typeof b != "string") {
								if (j === b) {
									k = !0;
									break
								}
							} else {
								if (m.filter(b, [j]).length > 0) {
									k = j;
									break
								}
							}
						}
						j = j[a]
					}
					e[h] = k
				}
			}
		}
		function w(a, b, c, e, f, g) {
			for (var h = 0,
			i = e.length; h < i; h++) {
				var j = e[h];
				if (j) {
					var k = !1;
					j = j[a];
					while (j) {
						if (j[d] === c) {
							k = e[j.sizset];
							break
						}
						j.nodeType === 1 && !g && (j[d] = c, j.sizset = h);
						if (j.nodeName.toLowerCase() === b) {
							k = j;
							break
						}
						j = j[a]
					}
					e[h] = k
				}
			}
		}
		var a = /((?:\((?:\([^()]+\)|[^()]+)+\)|\[(?:\[[^\[\]]*\]|['"][^'"]*['"]|[^\[\]'"]+)+\]|\\.|[^ >+~,(\[\\]+)+|[>+~])(\s*,\s*)?((?:.|\r|\n)*)/g,
		d = "sizcache" + (Math.random() + "").replace(".", ""),
		e = 0,
		g = Object.prototype.toString,
		h = !1,
		i = !0,
		j = /\\/g,
		k = /\r\n/g,
		l = /\W/; [0, 0].sort(function() {
			i = !1;
			return 0
		});
		var m = function(b, d, e, f) {
			e = e || [],
			d = d || c;
			var h = d;
			if (d.nodeType !== 1 && d.nodeType !== 9) {
				return []
			}
			if (!b || typeof b != "string") {
				return e
			}
			var i, j, k, l, n, q, r, t, u = !0,
			v = m.isXML(d),
			w = [],
			x = b;
			do {
				a.exec(""), i = a.exec(x);
				if (i) {
					x = i[3],
					w.push(i[1]);
					if (i[2]) {
						l = i[3];
						break
					}
				}
			} while ( i );
			if (w.length > 1 && p.exec(b)) {
				if (w.length === 2 && o.relative[w[0]]) {
					j = y(w[0] + w[1], d, f)
				} else {
					j = o.relative[w[0]] ? [d] : m(w.shift(), d);
					while (w.length) {
						b = w.shift(),
						o.relative[b] && (b += w.shift()),
						j = y(b, j, f)
					}
				}
			} else { ! f && w.length > 1 && d.nodeType === 9 && !v && o.match.ID.test(w[0]) && !o.match.ID.test(w[w.length - 1]) && (n = m.find(w.shift(), d, v), d = n.expr ? m.filter(n.expr, n.set)[0] : n.set[0]);
				if (d) {
					n = f ? {
						expr: w.pop(),
						set: s(f)
					}: m.find(w.pop(), w.length === 1 && (w[0] === "~" || w[0] === "+") && d.parentNode ? d.parentNode: d, v),
					j = n.expr ? m.filter(n.expr, n.set) : n.set,
					w.length > 0 ? k = s(j) : u = !1;
					while (w.length) {
						q = w.pop(),
						r = q,
						o.relative[q] ? r = w.pop() : q = "",
						r == null && (r = d),
						o.relative[q](k, r, v)
					}
				} else {
					k = w = []
				}
			}
			k || (k = j),
			k || m.error(q || b);
			if (g.call(k) === "[object Array]") {
				if (!u) {
					e.push.apply(e, k)
				} else {
					if (d && d.nodeType === 1) {
						for (t = 0; k[t] != null; t++) {
							k[t] && (k[t] === !0 || k[t].nodeType === 1 && m.contains(d, k[t])) && e.push(j[t])
						}
					} else {
						for (t = 0; k[t] != null; t++) {
							k[t] && k[t].nodeType === 1 && e.push(j[t])
						}
					}
				}
			} else {
				s(k, e)
			}
			l && (m(l, h, e, f), m.uniqueSort(e));
			return e
		};
		m.uniqueSort = function(a) {
			if (u) {
				h = i,
				a.sort(u);
				if (h) {
					for (var b = 1; b < a.length; b++) {
						a[b] === a[b - 1] && a.splice(b--, 1)
					}
				}
			}
			return a
		},
		m.matches = function(a, b) {
			return m(a, null, null, b)
		},
		m.matchesSelector = function(a, b) {
			return m(b, null, null, [a]).length > 0
		},
		m.find = function(a, b, c) {
			var d, e, f, g, h, i;
			if (!a) {
				return []
			}
			for (e = 0, f = o.order.length; e < f; e++) {
				h = o.order[e];
				if (g = o.leftMatch[h].exec(a)) {
					i = g[1],
					g.splice(1, 1);
					if (i.substr(i.length - 1) !== "\\") {
						g[1] = (g[1] || "").replace(j, ""),
						d = o.find[h](g, b, c);
						if (d != null) {
							a = a.replace(o.match[h], "");
							break
						}
					}
				}
			}
			d || (d = typeof b.getElementsByTagName != "undefined" ? b.getElementsByTagName("*") : []);
			return {
				set: d,
				expr: a
			}
		},
		m.filter = function(a, c, d, e) {
			var f, g, h, i, j, k, l, n, p, q = a,
			r = [],
			s = c,
			t = c && c[0] && m.isXML(c[0]);
			while (a && c.length) {
				for (h in o.filter) {
					if ((f = o.leftMatch[h].exec(a)) != null && f[2]) {
						k = o.filter[h],
						l = f[1],
						g = !1,
						f.splice(1, 1);
						if (l.substr(l.length - 1) === "\\") {
							continue
						}
						s === r && (r = []);
						if (o.preFilter[h]) {
							f = o.preFilter[h](f, s, d, r, e, t);
							if (!f) {
								g = i = !0
							} else {
								if (f === !0) {
									continue
								}
							}
						}
						if (f) {
							for (n = 0; (j = s[n]) != null; n++) {
								j && (i = k(j, f, n, s), p = e ^ i, d && i != null ? p ? g = !0 : s[n] = !1 : p && (r.push(j), g = !0))
							}
						}
						if (i !== b) {
							d || (s = r),
							a = a.replace(o.match[h], "");
							if (!g) {
								return []
							}
							break
						}
					}
				}
				if (a === q) {
					if (g == null) {
						m.error(a)
					} else {
						break
					}
				}
				q = a
			}
			return s
		},
		m.error = function(a) {
			throw new Error("Syntax error, unrecognized expression: " + a)
		};
		var n = m.getText = function(a) {
			var b, c, d = a.nodeType,
			e = "";
			if (d) {
				if (d === 1 || d === 9 || d === 11) {
					if (typeof a.textContent == "string") {
						return a.textContent
					}
					if (typeof a.innerText == "string") {
						return a.innerText.replace(k, "")
					}
					for (a = a.firstChild; a; a = a.nextSibling) {
						e += n(a)
					}
				} else {
					if (d === 3 || d === 4) {
						return a.nodeValue
					}
				}
			} else {
				for (b = 0; c = a[b]; b++) {
					c.nodeType !== 8 && (e += n(c))
				}
			}
			return e
		},
		o = m.selectors = {
			order: ["ID", "NAME", "TAG"],
			match: {
				ID: /#((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
				CLASS: /\.((?:[\w\u00c0-\uFFFF\-]|\\.)+)/,
				NAME: /\[name=['"]*((?:[\w\u00c0-\uFFFF\-]|\\.)+)['"]*\]/,
				ATTR: /\[\s*((?:[\w\u00c0-\uFFFF\-]|\\.)+)\s*(?:(\S?=)\s*(?:(['"])(.*?)\3|(#?(?:[\w\u00c0-\uFFFF\-]|\\.)*)|)|)\s*\]/,
				TAG: /^((?:[\w\u00c0-\uFFFF\*\-]|\\.)+)/,
				CHILD: /:(only|nth|last|first)-child(?:\(\s*(even|odd|(?:[+\-]?\d+|(?:[+\-]?\d*)?n\s*(?:[+\-]\s*\d+)?))\s*\))?/,
				POS: /:(nth|eq|gt|lt|first|last|even|odd)(?:\((\d*)\))?(?=[^\-]|$)/,
				PSEUDO: /:((?:[\w\u00c0-\uFFFF\-]|\\.)+)(?:\((['"]?)((?:\([^\)]+\)|[^\(\)]*)+)\2\))?/
			},
			leftMatch: {},
			attrMap: {
				"class": "className",
				"for": "htmlFor"
			},
			attrHandle: {
				href: function(a) {
					return a.getAttribute("href")
				},
				type: function(a) {
					return a.getAttribute("type")
				}
			},
			relative: {
				"+": function(a, b) {
					var c = typeof b == "string",
					d = c && !l.test(b),
					e = c && !d;
					d && (b = b.toLowerCase());
					for (var f = 0,
					g = a.length,
					h; f < g; f++) {
						if (h = a[f]) {
							while ((h = h.previousSibling) && h.nodeType !== 1) {}
							a[f] = e || h && h.nodeName.toLowerCase() === b ? h || !1 : h === b
						}
					}
					e && m.filter(b, a, !0)
				},
				">": function(a, b) {
					var c, d = typeof b == "string",
					e = 0,
					f = a.length;
					if (d && !l.test(b)) {
						b = b.toLowerCase();
						for (; e < f; e++) {
							c = a[e];
							if (c) {
								var g = c.parentNode;
								a[e] = g.nodeName.toLowerCase() === b ? g: !1
							}
						}
					} else {
						for (; e < f; e++) {
							c = a[e],
							c && (a[e] = d ? c.parentNode: c.parentNode === b)
						}
						d && m.filter(b, a, !0)
					}
				},
				"": function(a, b, c) {
					var d, f = e++,
					g = x;
					typeof b == "string" && !l.test(b) && (b = b.toLowerCase(), d = b, g = w),
					g("parentNode", b, f, a, d, c)
				},
				"~": function(a, b, c) {
					var d, f = e++,
					g = x;
					typeof b == "string" && !l.test(b) && (b = b.toLowerCase(), d = b, g = w),
					g("previousSibling", b, f, a, d, c)
				}
			},
			find: {
				ID: function(a, b, c) {
					if (typeof b.getElementById != "undefined" && !c) {
						var d = b.getElementById(a[1]);
						return d && d.parentNode ? [d] : []
					}
				},
				NAME: function(a, b) {
					if (typeof b.getElementsByName != "undefined") {
						var c = [],
						d = b.getElementsByName(a[1]);
						for (var e = 0,
						f = d.length; e < f; e++) {
							d[e].getAttribute("name") === a[1] && c.push(d[e])
						}
						return c.length === 0 ? null: c
					}
				},
				TAG: function(a, b) {
					if (typeof b.getElementsByTagName != "undefined") {
						return b.getElementsByTagName(a[1])
					}
				}
			},
			preFilter: {
				CLASS: function(a, b, c, d, e, f) {
					a = " " + a[1].replace(j, "") + " ";
					if (f) {
						return a
					}
					for (var g = 0,
					h; (h = b[g]) != null; g++) {
						h && (e ^ (h.className && (" " + h.className + " ").replace(/[\t\n\r]/g, " ").indexOf(a) >= 0) ? c || d.push(h) : c && (b[g] = !1))
					}
					return ! 1
				},
				ID: function(a) {
					return a[1].replace(j, "")
				},
				TAG: function(a, b) {
					return a[1].replace(j, "").toLowerCase()
				},
				CHILD: function(a) {
					if (a[1] === "nth") {
						a[2] || m.error(a[0]),
						a[2] = a[2].replace(/^\+|\s*/g, "");
						var b = /(-?)(\d*)(?:n([+\-]?\d*))?/.exec(a[2] === "even" && "2n" || a[2] === "odd" && "2n+1" || !/\D/.test(a[2]) && "0n+" + a[2] || a[2]);
						a[2] = b[1] + (b[2] || 1) - 0,
						a[3] = b[3] - 0
					} else {
						a[2] && m.error(a[0])
					}
					a[0] = e++;
					return a
				},
				ATTR: function(a, b, c, d, e, f) {
					var g = a[1] = a[1].replace(j, ""); ! f && o.attrMap[g] && (a[1] = o.attrMap[g]),
					a[4] = (a[4] || a[5] || "").replace(j, ""),
					a[2] === "~=" && (a[4] = " " + a[4] + " ");
					return a
				},
				PSEUDO: function(b, c, d, e, f) {
					if (b[1] === "not") {
						if ((a.exec(b[3]) || "").length > 1 || /^\w/.test(b[3])) {
							b[3] = m(b[3], null, null, c)
						} else {
							var g = m.filter(b[3], c, d, !0 ^ f);
							d || e.push.apply(e, g);
							return ! 1
						}
					} else {
						if (o.match.POS.test(b[0]) || o.match.CHILD.test(b[0])) {
							return ! 0
						}
					}
					return b
				},
				POS: function(a) {
					a.unshift(!0);
					return a
				}
			},
			filters: {
				enabled: function(a) {
					return a.disabled === !1 && a.type !== "hidden"
				},
				disabled: function(a) {
					return a.disabled === !0
				},
				checked: function(a) {
					return a.checked === !0
				},
				selected: function(a) {
					a.parentNode && a.parentNode.selectedIndex;
					return a.selected === !0
				},
				parent: function(a) {
					return !! a.firstChild
				},
				empty: function(a) {
					return ! a.firstChild
				},
				has: function(a, b, c) {
					return !! m(c[3], a).length
				},
				header: function(a) {
					return /h\d/i.test(a.nodeName)
				},
				text: function(a) {
					var b = a.getAttribute("type"),
					c = a.type;
					return a.nodeName.toLowerCase() === "input" && "text" === c && (b === c || b === null)
				},
				radio: function(a) {
					return a.nodeName.toLowerCase() === "input" && "radio" === a.type
				},
				checkbox: function(a) {
					return a.nodeName.toLowerCase() === "input" && "checkbox" === a.type
				},
				file: function(a) {
					return a.nodeName.toLowerCase() === "input" && "file" === a.type
				},
				password: function(a) {
					return a.nodeName.toLowerCase() === "input" && "password" === a.type
				},
				submit: function(a) {
					var b = a.nodeName.toLowerCase();
					return (b === "input" || b === "button") && "submit" === a.type
				},
				image: function(a) {
					return a.nodeName.toLowerCase() === "input" && "image" === a.type
				},
				reset: function(a) {
					var b = a.nodeName.toLowerCase();
					return (b === "input" || b === "button") && "reset" === a.type
				},
				button: function(a) {
					var b = a.nodeName.toLowerCase();
					return b === "input" && "button" === a.type || b === "button"
				},
				input: function(a) {
					return /input|select|textarea|button/i.test(a.nodeName)
				},
				focus: function(a) {
					return a === a.ownerDocument.activeElement
				}
			},
			setFilters: {
				first: function(a, b) {
					return b === 0
				},
				last: function(a, b, c, d) {
					return b === d.length - 1
				},
				even: function(a, b) {
					return b % 2 === 0
				},
				odd: function(a, b) {
					return b % 2 === 1
				},
				lt: function(a, b, c) {
					return b < c[3] - 0
				},
				gt: function(a, b, c) {
					return b > c[3] - 0
				},
				nth: function(a, b, c) {
					return c[3] - 0 === b
				},
				eq: function(a, b, c) {
					return c[3] - 0 === b
				}
			},
			filter: {
				PSEUDO: function(a, b, c, d) {
					var e = b[1],
					f = o.filters[e];
					if (f) {
						return f(a, c, b, d)
					}
					if (e === "contains") {
						return (a.textContent || a.innerText || n([a]) || "").indexOf(b[3]) >= 0
					}
					if (e === "not") {
						var g = b[3];
						for (var h = 0,
						i = g.length; h < i; h++) {
							if (g[h] === a) {
								return ! 1
							}
						}
						return ! 0
					}
					m.error(e)
				},
				CHILD: function(a, b) {
					var c, e, f, g, h, i, j, k = b[1],
					l = a;
					switch (k) {
					case "only":
					case "first":
						while (l = l.previousSibling) {
							if (l.nodeType === 1) {
								return ! 1
							}
						}
						if (k === "first") {
							return ! 0
						}
						l = a;
					case "last":
						while (l = l.nextSibling) {
							if (l.nodeType === 1) {
								return ! 1
							}
						}
						return ! 0;
					case "nth":
						c = b[2],
						e = b[3];
						if (c === 1 && e === 0) {
							return ! 0
						}
						f = b[0],
						g = a.parentNode;
						if (g && (g[d] !== f || !a.nodeIndex)) {
							i = 0;
							for (l = g.firstChild; l; l = l.nextSibling) {
								l.nodeType === 1 && (l.nodeIndex = ++i)
							}
							g[d] = f
						}
						j = a.nodeIndex - e;
						return c === 0 ? j === 0 : j % c === 0 && j / c >= 0
					}
				},
				ID: function(a, b) {
					return a.nodeType === 1 && a.getAttribute("id") === b
				},
				TAG: function(a, b) {
					return b === "*" && a.nodeType === 1 || !!a.nodeName && a.nodeName.toLowerCase() === b
				},
				CLASS: function(a, b) {
					return (" " + (a.className || a.getAttribute("class")) + " ").indexOf(b) > -1
				},
				ATTR: function(a, b) {
					var c = b[1],
					d = m.attr ? m.attr(a, c) : o.attrHandle[c] ? o.attrHandle[c](a) : a[c] != null ? a[c] : a.getAttribute(c),
					e = d + "",
					f = b[2],
					g = b[4];
					return d == null ? f === "!=": !f && m.attr ? d != null: f === "=" ? e === g: f === "*=" ? e.indexOf(g) >= 0 : f === "~=" ? (" " + e + " ").indexOf(g) >= 0 : g ? f === "!=" ? e !== g: f === "^=" ? e.indexOf(g) === 0 : f === "$=" ? e.substr(e.length - g.length) === g: f === "|=" ? e === g || e.substr(0, g.length + 1) === g + "-": !1 : e && d !== !1
				},
				POS: function(a, b, c, d) {
					var e = b[2],
					f = o.setFilters[e];
					if (f) {
						return f(a, c, b, d)
					}
				}
			}
		},
		p = o.match.POS,
		q = function(a, b) {
			return "\\" + (b - 0 + 1)
		};
		for (var r in o.match) {
			o.match[r] = new RegExp(o.match[r].source + /(?![^\[]*\])(?![^\(]*\))/.source),
			o.leftMatch[r] = new RegExp(/(^(?:.|\r|\n)*?)/.source + o.match[r].source.replace(/\\(\d+)/g, q))
		}
		o.match.globalPOS = p;
		var s = function(a, b) {
			a = Array.prototype.slice.call(a, 0);
			if (b) {
				b.push.apply(b, a);
				return b
			}
			return a
		};
		try {
			Array.prototype.slice.call(c.documentElement.childNodes, 0)[0].nodeType
		} catch(t) {
			s = function(a, b) {
				var c = 0,
				d = b || [];
				if (g.call(a) === "[object Array]") {
					Array.prototype.push.apply(d, a)
				} else {
					if (typeof a.length == "number") {
						for (var e = a.length; c < e; c++) {
							d.push(a[c])
						}
					} else {
						for (; a[c]; c++) {
							d.push(a[c])
						}
					}
				}
				return d
			}
		}
		var u, v;
		c.documentElement.compareDocumentPosition ? u = function(a, b) {
			if (a === b) {
				h = !0;
				return 0
			}
			if (!a.compareDocumentPosition || !b.compareDocumentPosition) {
				return a.compareDocumentPosition ? -1 : 1
			}
			return a.compareDocumentPosition(b) & 4 ? -1 : 1
		}: (u = function(a, b) {
			if (a === b) {
				h = !0;
				return 0
			}
			if (a.sourceIndex && b.sourceIndex) {
				return a.sourceIndex - b.sourceIndex
			}
			var c, d, e = [],
			f = [],
			g = a.parentNode,
			i = b.parentNode,
			j = g;
			if (g === i) {
				return v(a, b)
			}
			if (!g) {
				return - 1
			}
			if (!i) {
				return 1
			}
			while (j) {
				e.unshift(j),
				j = j.parentNode
			}
			j = i;
			while (j) {
				f.unshift(j),
				j = j.parentNode
			}
			c = e.length,
			d = f.length;
			for (var k = 0; k < c && k < d; k++) {
				if (e[k] !== f[k]) {
					return v(e[k], f[k])
				}
			}
			return k === c ? v(a, f[k], -1) : v(e[k], b, 1)
		},
		v = function(a, b, c) {
			if (a === b) {
				return c
			}
			var d = a.nextSibling;
			while (d) {
				if (d === b) {
					return - 1
				}
				d = d.nextSibling
			}
			return 1
		}),
		function() {
			var a = c.createElement("div"),
			d = "script" + (new Date).getTime(),
			e = c.documentElement;
			a.innerHTML = "<a name='" + d + "'/>",
			e.insertBefore(a, e.firstChild),
			c.getElementById(d) && (o.find.ID = function(a, c, d) {
				if (typeof c.getElementById != "undefined" && !d) {
					var e = c.getElementById(a[1]);
					return e ? e.id === a[1] || typeof e.getAttributeNode != "undefined" && e.getAttributeNode("id").nodeValue === a[1] ? [e] : b: []
				}
			},
			o.filter.ID = function(a, b) {
				var c = typeof a.getAttributeNode != "undefined" && a.getAttributeNode("id");
				return a.nodeType === 1 && c && c.nodeValue === b
			}),
			e.removeChild(a),
			e = a = null
		} (),
		function() {
			var a = c.createElement("div");
			a.appendChild(c.createComment("")),
			a.getElementsByTagName("*").length > 0 && (o.find.TAG = function(a, b) {
				var c = b.getElementsByTagName(a[1]);
				if (a[1] === "*") {
					var d = [];
					for (var e = 0; c[e]; e++) {
						c[e].nodeType === 1 && d.push(c[e])
					}
					c = d
				}
				return c
			}),
			a.innerHTML = "<a href='#'></a>",
			a.firstChild && typeof a.firstChild.getAttribute != "undefined" && a.firstChild.getAttribute("href") !== "#" && (o.attrHandle.href = function(a) {
				return a.getAttribute("href", 2)
			}),
			a = null
		} (),
		c.querySelectorAll &&
		function() {
			var a = m,
			b = c.createElement("div"),
			d = "__sizzle__";
			b.innerHTML = "<p class='TEST'></p>";
			if (!b.querySelectorAll || b.querySelectorAll(".TEST").length !== 0) {
				m = function(b, e, f, g) {
					e = e || c;
					if (!g && !m.isXML(e)) {
						var h = /^(\w+$)|^\.([\w\-]+$)|^#([\w\-]+$)/.exec(b);
						if (h && (e.nodeType === 1 || e.nodeType === 9)) {
							if (h[1]) {
								return s(e.getElementsByTagName(b), f)
							}
							if (h[2] && o.find.CLASS && e.getElementsByClassName) {
								return s(e.getElementsByClassName(h[2]), f)
							}
						}
						if (e.nodeType === 9) {
							if (b === "body" && e.body) {
								return s([e.body], f)
							}
							if (h && h[3]) {
								var i = e.getElementById(h[3]);
								if (!i || !i.parentNode) {
									return s([], f)
								}
								if (i.id === h[3]) {
									return s([i], f)
								}
							}
							try {
								return s(e.querySelectorAll(b), f)
							} catch(j) {}
						} else {
							if (e.nodeType === 1 && e.nodeName.toLowerCase() !== "object") {
								var k = e,
								l = e.getAttribute("id"),
								n = l || d,
								p = e.parentNode,
								q = /^\s*[+~]/.test(b);
								l ? n = n.replace(/'/g, "\\$&") : e.setAttribute("id", n),
								q && p && (e = e.parentNode);
								try {
									if (!q || p) {
										return s(e.querySelectorAll("[id='" + n + "'] " + b), f)
									}
								} catch(r) {} finally {
									l || k.removeAttribute("id")
								}
							}
						}
					}
					return a(b, e, f, g)
				};
				for (var e in a) {
					m[e] = a[e]
				}
				b = null
			}
		} (),
		function() {
			var a = c.documentElement,
			b = a.matchesSelector || a.mozMatchesSelector || a.webkitMatchesSelector || a.msMatchesSelector;
			if (b) {
				var d = !b.call(c.createElement("div"), "div"),
				e = !1;
				try {
					b.call(c.documentElement, "[test!='']:sizzle")
				} catch(f) {
					e = !0
				}
				m.matchesSelector = function(a, c) {
					c = c.replace(/\=\s*([^'"\]]*)\s*\]/g, "='$1']");
					if (!m.isXML(a)) {
						try {
							if (e || !o.match.PSEUDO.test(c) && !/!=/.test(c)) {
								var f = b.call(a, c);
								if (f || !d || a.document && a.document.nodeType !== 11) {
									return f
								}
							}
						} catch(g) {}
					}
					return m(c, null, null, [a]).length > 0
				}
			}
		} (),
		function() {
			var a = c.createElement("div");
			a.innerHTML = "<div class='test e'></div><div class='test'></div>";
			if ( !! a.getElementsByClassName && a.getElementsByClassName("e").length !== 0) {
				a.lastChild.className = "e";
				if (a.getElementsByClassName("e").length === 1) {
					return
				}
				o.order.splice(1, 0, "CLASS"),
				o.find.CLASS = function(a, b, c) {
					if (typeof b.getElementsByClassName != "undefined" && !c) {
						return b.getElementsByClassName(a[1])
					}
				},
				a = null
			}
		} (),
		c.documentElement.contains ? m.contains = function(a, b) {
			return a !== b && (a.contains ? a.contains(b) : !0)
		}: c.documentElement.compareDocumentPosition ? m.contains = function(a, b) {
			return !! (a.compareDocumentPosition(b) & 16)
		}: m.contains = function() {
			return ! 1
		},
		m.isXML = function(a) {
			var b = (a ? a.ownerDocument || a: 0).documentElement;
			return b ? b.nodeName !== "HTML": !1
		};
		var y = function(a, b, c) {
			var d, e = [],
			f = "",
			g = b.nodeType ? [b] : b;
			while (d = o.match.PSEUDO.exec(a)) {
				f += d[0],
				a = a.replace(o.match.PSEUDO, "")
			}
			a = o.relative[a] ? a + "*": a;
			for (var h = 0,
			i = g.length; h < i; h++) {
				m(a, g[h], e, c)
			}
			return m.filter(f, e)
		};
		m.attr = f.attr,
		m.selectors.attrMap = {},
		f.find = m,
		f.expr = m.selectors,
		f.expr[":"] = f.expr.filters,
		f.unique = m.uniqueSort,
		f.text = m.getText,
		f.isXMLDoc = m.isXML,
		f.contains = m.contains
	} ();
	var L = /Until$/,
	M = /^(?:parents|prevUntil|prevAll)/,
	N = /,/,
	O = /^.[^:#\[\.,]*$/,
	P = Array.prototype.slice,
	Q = f.expr.match.globalPOS,
	R = {
		children: !0,
		contents: !0,
		next: !0,
		prev: !0
	};
	f.fn.extend({
		find: function(a) {
			var b = this,
			c, d;
			if (typeof a != "string") {
				return f(a).filter(function() {
					for (c = 0, d = b.length; c < d; c++) {
						if (f.contains(b[c], this)) {
							return ! 0
						}
					}
				})
			}
			var e = this.pushStack("", "find", a),
			g,
			h,
			i;
			for (c = 0, d = this.length; c < d; c++) {
				g = e.length,
				f.find(a, this[c], e);
				if (c > 0) {
					for (h = g; h < e.length; h++) {
						for (i = 0; i < g; i++) {
							if (e[i] === e[h]) {
								e.splice(h--, 1);
								break
							}
						}
					}
				}
			}
			return e
		},
		has: function(a) {
			var b = f(a);
			return this.filter(function() {
				for (var a = 0,
				c = b.length; a < c; a++) {
					if (f.contains(this, b[a])) {
						return ! 0
					}
				}
			})
		},
		not: function(a) {
			return this.pushStack(T(this, a, !1), "not", a)
		},
		filter: function(a) {
			return this.pushStack(T(this, a, !0), "filter", a)
		},
		is: function(a) {
			return !! a && (typeof a == "string" ? Q.test(a) ? f(a, this.context).index(this[0]) >= 0 : f.filter(a, this).length > 0 : this.filter(a).length > 0)
		},
		closest: function(a, b) {
			var c = [],
			d,
			e,
			g = this[0];
			if (f.isArray(a)) {
				var h = 1;
				while (g && g.ownerDocument && g !== b) {
					for (d = 0; d < a.length; d++) {
						f(g).is(a[d]) && c.push({
							selector: a[d],
							elem: g,
							level: h
						})
					}
					g = g.parentNode,
					h++
				}
				return c
			}
			var i = Q.test(a) || typeof a != "string" ? f(a, b || this.context) : 0;
			for (d = 0, e = this.length; d < e; d++) {
				g = this[d];
				while (g) {
					if (i ? i.index(g) > -1 : f.find.matchesSelector(g, a)) {
						c.push(g);
						break
					}
					g = g.parentNode;
					if (!g || !g.ownerDocument || g === b || g.nodeType === 11) {
						break
					}
				}
			}
			c = c.length > 1 ? f.unique(c) : c;
			return this.pushStack(c, "closest", a)
		},
		index: function(a) {
			if (!a) {
				return this[0] && this[0].parentNode ? this.prevAll().length: -1
			}
			if (typeof a == "string") {
				return f.inArray(this[0], f(a))
			}
			return f.inArray(a.jquery ? a[0] : a, this)
		},
		add: function(a, b) {
			var c = typeof a == "string" ? f(a, b) : f.makeArray(a && a.nodeType ? [a] : a),
			d = f.merge(this.get(), c);
			return this.pushStack(S(c[0]) || S(d[0]) ? d: f.unique(d))
		},
		andSelf: function() {
			return this.add(this.prevObject)
		}
	}),
	f.each({
		parent: function(a) {
			var b = a.parentNode;
			return b && b.nodeType !== 11 ? b: null
		},
		parents: function(a) {
			return f.dir(a, "parentNode")
		},
		parentsUntil: function(a, b, c) {
			return f.dir(a, "parentNode", c)
		},
		next: function(a) {
			return f.nth(a, 2, "nextSibling")
		},
		prev: function(a) {
			return f.nth(a, 2, "previousSibling")
		},
		nextAll: function(a) {
			return f.dir(a, "nextSibling")
		},
		prevAll: function(a) {
			return f.dir(a, "previousSibling")
		},
		nextUntil: function(a, b, c) {
			return f.dir(a, "nextSibling", c)
		},
		prevUntil: function(a, b, c) {
			return f.dir(a, "previousSibling", c)
		},
		siblings: function(a) {
			return f.sibling((a.parentNode || {}).firstChild, a)
		},
		children: function(a) {
			return f.sibling(a.firstChild)
		},
		contents: function(a) {
			return f.nodeName(a, "iframe") ? a.contentDocument || a.contentWindow.document: f.makeArray(a.childNodes)
		}
	},
	function(a, b) {
		f.fn[a] = function(c, d) {
			var e = f.map(this, b, c);
			L.test(a) || (d = c),
			d && typeof d == "string" && (e = f.filter(d, e)),
			e = this.length > 1 && !R[a] ? f.unique(e) : e,
			(this.length > 1 || N.test(d)) && M.test(a) && (e = e.reverse());
			return this.pushStack(e, a, P.call(arguments).join(","))
		}
	}),
	f.extend({
		filter: function(a, b, c) {
			c && (a = ":not(" + a + ")");
			return b.length === 1 ? f.find.matchesSelector(b[0], a) ? [b[0]] : [] : f.find.matches(a, b)
		},
		dir: function(a, c, d) {
			var e = [],
			g = a[c];
			while (g && g.nodeType !== 9 && (d === b || g.nodeType !== 1 || !f(g).is(d))) {
				g.nodeType === 1 && e.push(g),
				g = g[c]
			}
			return e
		},
		nth: function(a, b, c, d) {
			b = b || 1;
			var e = 0;
			for (; a; a = a[c]) {
				if (a.nodeType === 1 && ++e === b) {
					break
				}
			}
			return a
		},
		sibling: function(a, b) {
			var c = [];
			for (; a; a = a.nextSibling) {
				a.nodeType === 1 && a !== b && c.push(a)
			}
			return c
		}
	});
	var V = "abbr|article|aside|audio|bdi|canvas|data|datalist|details|figcaption|figure|footer|header|hgroup|mark|meter|nav|output|progress|section|summary|time|video",
	W = / jQuery\d+="(?:\d+|null)"/g,
	X = /^\s+/,
	Y = /<(?!area|br|col|embed|hr|img|input|link|meta|param)(([\w:]+)[^>]*)\/>/ig,
	Z = /<([\w:]+)/,
	$ = /<tbody/i,
	_ = /<|&#?\w+;/,
	ba = /<(?:script|style)/i,
	bb = /<(?:script|object|embed|option|style)/i,
	bc = new RegExp("<(?:" + V + ")[\\s/>]", "i"),
	bd = /checked\s*(?:[^=]|=\s*.checked.)/i,
	be = /\/(java|ecma)script/i,
	bf = /^\s*<!(?:\[CDATA\[|\-\-)/,
	bg = {
		option: [1, "<select multiple='multiple'>", "</select>"],
		legend: [1, "<fieldset>", "</fieldset>"],
		thead: [1, "<table>", "</table>"],
		tr: [2, "<table><tbody>", "</tbody></table>"],
		td: [3, "<table><tbody><tr>", "</tr></tbody></table>"],
		col: [2, "<table><tbody></tbody><colgroup>", "</colgroup></table>"],
		area: [1, "<map>", "</map>"],
		_default: [0, "", ""]
	},
	bh = U(c);
	bg.optgroup = bg.option,
	bg.tbody = bg.tfoot = bg.colgroup = bg.caption = bg.thead,
	bg.th = bg.td,
	f.support.htmlSerialize || (bg._default = [1, "div<div>", "</div>"]),
	f.fn.extend({
		text: function(a) {
			return f.access(this,
			function(a) {
				return a === b ? f.text(this) : this.empty().append((this[0] && this[0].ownerDocument || c).createTextNode(a))
			},
			null, a, arguments.length)
		},
		wrapAll: function(a) {
			if (f.isFunction(a)) {
				return this.each(function(b) {
					f(this).wrapAll(a.call(this, b))
				})
			}
			if (this[0]) {
				var b = f(a, this[0].ownerDocument).eq(0).clone(!0);
				this[0].parentNode && b.insertBefore(this[0]),
				b.map(function() {
					var a = this;
					while (a.firstChild && a.firstChild.nodeType === 1) {
						a = a.firstChild
					}
					return a
				}).append(this)
			}
			return this
		},
		wrapInner: function(a) {
			if (f.isFunction(a)) {
				return this.each(function(b) {
					f(this).wrapInner(a.call(this, b))
				})
			}
			return this.each(function() {
				var b = f(this),
				c = b.contents();
				c.length ? c.wrapAll(a) : b.append(a)
			})
		},
		wrap: function(a) {
			var b = f.isFunction(a);
			return this.each(function(c) {
				f(this).wrapAll(b ? a.call(this, c) : a)
			})
		},
		unwrap: function() {
			return this.parent().each(function() {
				f.nodeName(this, "body") || f(this).replaceWith(this.childNodes)
			}).end()
		},
		append: function() {
			return this.domManip(arguments, !0,
			function(a) {
				this.nodeType === 1 && this.appendChild(a)
			})
		},
		prepend: function() {
			return this.domManip(arguments, !0,
			function(a) {
				this.nodeType === 1 && this.insertBefore(a, this.firstChild)
			})
		},
		before: function() {
			if (this[0] && this[0].parentNode) {
				return this.domManip(arguments, !1,
				function(a) {
					this.parentNode.insertBefore(a, this)
				})
			}
			if (arguments.length) {
				var a = f.clean(arguments);
				a.push.apply(a, this.toArray());
				return this.pushStack(a, "before", arguments)
			}
		},
		after: function() {
			if (this[0] && this[0].parentNode) {
				return this.domManip(arguments, !1,
				function(a) {
					this.parentNode.insertBefore(a, this.nextSibling)
				})
			}
			if (arguments.length) {
				var a = this.pushStack(this, "after", arguments);
				a.push.apply(a, f.clean(arguments));
				return a
			}
		},
		remove: function(a, b) {
			for (var c = 0,
			d; (d = this[c]) != null; c++) {
				if (!a || f.filter(a, [d]).length) { ! b && d.nodeType === 1 && (f.cleanData(d.getElementsByTagName("*")), f.cleanData([d])),
					d.parentNode && d.parentNode.removeChild(d)
				}
			}
			return this
		},
		empty: function() {
			for (var a = 0,
			b; (b = this[a]) != null; a++) {
				b.nodeType === 1 && f.cleanData(b.getElementsByTagName("*"));
				while (b.firstChild) {
					b.removeChild(b.firstChild)
				}
			}
			return this
		},
		clone: function(a, b) {
			a = a == null ? !1 : a,
			b = b == null ? a: b;
			return this.map(function() {
				return f.clone(this, a, b)
			})
		},
		html: function(a) {
			return f.access(this,
			function(a) {
				var c = this[0] || {},
				d = 0,
				e = this.length;
				if (a === b) {
					return c.nodeType === 1 ? c.innerHTML.replace(W, "") : null
				}
				if (typeof a == "string" && !ba.test(a) && (f.support.leadingWhitespace || !X.test(a)) && !bg[(Z.exec(a) || ["", ""])[1].toLowerCase()]) {
					a = a.replace(Y, "<$1></$2>");
					try {
						for (; d < e; d++) {
							c = this[d] || {},
							c.nodeType === 1 && (f.cleanData(c.getElementsByTagName("*")), c.innerHTML = a)
						}
						c = 0
					} catch(g) {}
				}
				c && this.empty().append(a)
			},
			null, a, arguments.length)
		},
		replaceWith: function(a) {
			if (this[0] && this[0].parentNode) {
				if (f.isFunction(a)) {
					return this.each(function(b) {
						var c = f(this),
						d = c.html();
						c.replaceWith(a.call(this, b, d))
					})
				}
				typeof a != "string" && (a = f(a).detach());
				return this.each(function() {
					var b = this.nextSibling,
					c = this.parentNode;
					f(this).remove(),
					b ? f(b).before(a) : f(c).append(a)
				})
			}
			return this.length ? this.pushStack(f(f.isFunction(a) ? a() : a), "replaceWith", a) : this
		},
		detach: function(a) {
			return this.remove(a, !0)
		},
		domManip: function(a, c, d) {
			var e, g, h, i, j = a[0],
			k = [];
			if (!f.support.checkClone && arguments.length === 3 && typeof j == "string" && bd.test(j)) {
				return this.each(function() {
					f(this).domManip(a, c, d, !0)
				})
			}
			if (f.isFunction(j)) {
				return this.each(function(e) {
					var g = f(this);
					a[0] = j.call(this, e, c ? g.html() : b),
					g.domManip(a, c, d)
				})
			}
			if (this[0]) {
				i = j && j.parentNode,
				f.support.parentNode && i && i.nodeType === 11 && i.childNodes.length === this.length ? e = {
					fragment: i
				}: e = f.buildFragment(a, this, k),
				h = e.fragment,
				h.childNodes.length === 1 ? g = h = h.firstChild: g = h.firstChild;
				if (g) {
					c = c && f.nodeName(g, "tr");
					for (var l = 0,
					m = this.length,
					n = m - 1; l < m; l++) {
						d.call(c ? bi(this[l], g) : this[l], e.cacheable || m > 1 && l < n ? f.clone(h, !0, !0) : h)
					}
				}
				k.length && f.each(k,
				function(a, b) {
					b.src ? f.ajax({
						type: "GET",
						global: !1,
						url: b.src,
						async: !1,
						dataType: "script"
					}) : f.globalEval((b.text || b.textContent || b.innerHTML || "").replace(bf, "/*$0*/")),
					b.parentNode && b.parentNode.removeChild(b)
				})
			}
			return this
		}
	}),
	f.buildFragment = function(a, b, d) {
		var e, g, h, i, j = a[0];
		b && b[0] && (i = b[0].ownerDocument || b[0]),
		i.createDocumentFragment || (i = c),
		a.length === 1 && typeof j == "string" && j.length < 512 && i === c && j.charAt(0) === "<" && !bb.test(j) && (f.support.checkClone || !bd.test(j)) && (f.support.html5Clone || !bc.test(j)) && (g = !0, h = f.fragments[j], h && h !== 1 && (e = h)),
		e || (e = i.createDocumentFragment(), f.clean(a, i, e, d)),
		g && (f.fragments[j] = h ? e: 1);
		return {
			fragment: e,
			cacheable: g
		}
	},
	f.fragments = {},
	f.each({
		appendTo: "append",
		prependTo: "prepend",
		insertBefore: "before",
		insertAfter: "after",
		replaceAll: "replaceWith"
	},
	function(a, b) {
		f.fn[a] = function(c) {
			var d = [],
			e = f(c),
			g = this.length === 1 && this[0].parentNode;
			if (g && g.nodeType === 11 && g.childNodes.length === 1 && e.length === 1) {
				e[b](this[0]);
				return this
			}
			for (var h = 0,
			i = e.length; h < i; h++) {
				var j = (h > 0 ? this.clone(!0) : this).get();
				f(e[h])[b](j),
				d = d.concat(j)
			}
			return this.pushStack(d, a, e.selector)
		}
	}),
	f.extend({
		clone: function(a, b, c) {
			var d, e, g, h = f.support.html5Clone || f.isXMLDoc(a) || !bc.test("<" + a.nodeName + ">") ? a.cloneNode(!0) : bo(a);
			if ((!f.support.noCloneEvent || !f.support.noCloneChecked) && (a.nodeType === 1 || a.nodeType === 11) && !f.isXMLDoc(a)) {
				bk(a, h),
				d = bl(a),
				e = bl(h);
				for (g = 0; d[g]; ++g) {
					e[g] && bk(d[g], e[g])
				}
			}
			if (b) {
				bj(a, h);
				if (c) {
					d = bl(a),
					e = bl(h);
					for (g = 0; d[g]; ++g) {
						bj(d[g], e[g])
					}
				}
			}
			d = e = null;
			return h
		},
		clean: function(a, b, d, e) {
			var g, h, i, j = [];
			b = b || c,
			typeof b.createElement == "undefined" && (b = b.ownerDocument || b[0] && b[0].ownerDocument || c);
			for (var k = 0,
			l; (l = a[k]) != null; k++) {
				typeof l == "number" && (l += "");
				if (!l) {
					continue
				}
				if (typeof l == "string") {
					if (!_.test(l)) {
						l = b.createTextNode(l)
					} else {
						l = l.replace(Y, "<$1></$2>");
						var m = (Z.exec(l) || ["", ""])[1].toLowerCase(),
						n = bg[m] || bg._default,
						o = n[0],
						p = b.createElement("div"),
						q = bh.childNodes,
						r;
						b === c ? bh.appendChild(p) : U(b).appendChild(p),
						p.innerHTML = n[1] + l + n[2];
						while (o--) {
							p = p.lastChild
						}
						if (!f.support.tbody) {
							var s = $.test(l),
							t = m === "table" && !s ? p.firstChild && p.firstChild.childNodes: n[1] === "<table>" && !s ? p.childNodes: [];
							for (i = t.length - 1; i >= 0; --i) {
								f.nodeName(t[i], "tbody") && !t[i].childNodes.length && t[i].parentNode.removeChild(t[i])
							}
						} ! f.support.leadingWhitespace && X.test(l) && p.insertBefore(b.createTextNode(X.exec(l)[0]), p.firstChild),
						l = p.childNodes,
						p && (p.parentNode.removeChild(p), q.length > 0 && (r = q[q.length - 1], r && r.parentNode && r.parentNode.removeChild(r)))
					}
				}
				var u;
				if (!f.support.appendChecked) {
					if (l[0] && typeof(u = l.length) == "number") {
						for (i = 0; i < u; i++) {
							bn(l[i])
						}
					} else {
						bn(l)
					}
				}
				l.nodeType ? j.push(l) : j = f.merge(j, l)
			}
			if (d) {
				g = function(a) {
					return ! a.type || be.test(a.type)
				};
				for (k = 0; j[k]; k++) {
					h = j[k];
					if (e && f.nodeName(h, "script") && (!h.type || be.test(h.type))) {
						e.push(h.parentNode ? h.parentNode.removeChild(h) : h)
					} else {
						if (h.nodeType === 1) {
							var v = f.grep(h.getElementsByTagName("script"), g);
							j.splice.apply(j, [k + 1, 0].concat(v))
						}
						d.appendChild(h)
					}
				}
			}
			return j
		},
		cleanData: function(a) {
			var b, c, d = f.cache,
			e = f.event.special,
			g = f.support.deleteExpando;
			for (var h = 0,
			i; (i = a[h]) != null; h++) {
				if (i.nodeName && f.noData[i.nodeName.toLowerCase()]) {
					continue
				}
				c = i[f.expando];
				if (c) {
					b = d[c];
					if (b && b.events) {
						for (var j in b.events) {
							e[j] ? f.event.remove(i, j) : f.removeEvent(i, j, b.handle)
						}
						b.handle && (b.handle.elem = null)
					}
					g ? delete i[f.expando] : i.removeAttribute && i.removeAttribute(f.expando),
					delete d[c]
				}
			}
		}
	});
	var bp = /alpha\([^)]*\)/i,
	bq = /opacity=([^)]*)/,
	br = /([A-Z]|^ms)/g,
	bs = /^[\-+]?(?:\d*\.)?\d+$/i,
	bt = /^-?(?:\d*\.)?\d+(?!px)[^\d\s]+$/i,
	bu = /^([\-+])=([\-+.\de]+)/,
	bv = /^margin/,
	bw = {
		position: "absolute",
		visibility: "hidden",
		display: "block"
	},
	bx = ["Top", "Right", "Bottom", "Left"],
	by,
	bz,
	bA;
	f.fn.css = function(a, c) {
		return f.access(this,
		function(a, c, d) {
			return d !== b ? f.style(a, c, d) : f.css(a, c)
		},
		a, c, arguments.length > 1)
	},
	f.extend({
		cssHooks: {
			opacity: {
				get: function(a, b) {
					if (b) {
						var c = by(a, "opacity");
						return c === "" ? "1": c
					}
					return a.style.opacity
				}
			}
		},
		cssNumber: {
			fillOpacity: !0,
			fontWeight: !0,
			lineHeight: !0,
			opacity: !0,
			orphans: !0,
			widows: !0,
			zIndex: !0,
			zoom: !0
		},
		cssProps: {
			"float": f.support.cssFloat ? "cssFloat": "styleFloat"
		},
		style: function(a, c, d, e) {
			if ( !! a && a.nodeType !== 3 && a.nodeType !== 8 && !!a.style) {
				var g, h, i = f.camelCase(c),
				j = a.style,
				k = f.cssHooks[i];
				c = f.cssProps[i] || i;
				if (d === b) {
					if (k && "get" in k && (g = k.get(a, !1, e)) !== b) {
						return g
					}
					return j[c]
				}
				h = typeof d,
				h === "string" && (g = bu.exec(d)) && (d = +(g[1] + 1) * +g[2] + parseFloat(f.css(a, c)), h = "number");
				if (d == null || h === "number" && isNaN(d)) {
					return
				}
				h === "number" && !f.cssNumber[i] && (d += "px");
				if (!k || !("set" in k) || (d = k.set(a, d)) !== b) {
					try {
						j[c] = d
					} catch(l) {}
				}
			}
		},
		css: function(a, c, d) {
			var e, g;
			c = f.camelCase(c),
			g = f.cssHooks[c],
			c = f.cssProps[c] || c,
			c === "cssFloat" && (c = "float");
			if (g && "get" in g && (e = g.get(a, !0, d)) !== b) {
				return e
			}
			if (by) {
				return by(a, c)
			}
		},
		swap: function(a, b, c) {
			var d = {},
			e, f;
			for (f in b) {
				d[f] = a.style[f],
				a.style[f] = b[f]
			}
			e = c.call(a);
			for (f in b) {
				a.style[f] = d[f]
			}
			return e
		}
	}),
	f.curCSS = f.css,
	c.defaultView && c.defaultView.getComputedStyle && (bz = function(a, b) {
		var c, d, e, g, h = a.style;
		b = b.replace(br, "-$1").toLowerCase(),
		(d = a.ownerDocument.defaultView) && (e = d.getComputedStyle(a, null)) && (c = e.getPropertyValue(b), c === "" && !f.contains(a.ownerDocument.documentElement, a) && (c = f.style(a, b))),
		!f.support.pixelMargin && e && bv.test(b) && bt.test(c) && (g = h.width, h.width = c, c = e.width, h.width = g);
		return c
	}),
	c.documentElement.currentStyle && (bA = function(a, b) {
		var c, d, e, f = a.currentStyle && a.currentStyle[b],
		g = a.style;
		f == null && g && (e = g[b]) && (f = e),
		bt.test(f) && (c = g.left, d = a.runtimeStyle && a.runtimeStyle.left, d && (a.runtimeStyle.left = a.currentStyle.left), g.left = b === "fontSize" ? "1em": f, f = g.pixelLeft + "px", g.left = c, d && (a.runtimeStyle.left = d));
		return f === "" ? "auto": f
	}),
	by = bz || bA,
	f.each(["height", "width"],
	function(a, b) {
		f.cssHooks[b] = {
			get: function(a, c, d) {
				if (c) {
					return a.offsetWidth !== 0 ? bB(a, b, d) : f.swap(a, bw,
					function() {
						return bB(a, b, d)
					})
				}
			},
			set: function(a, b) {
				return bs.test(b) ? b + "px": b
			}
		}
	}),
	f.support.opacity || (f.cssHooks.opacity = {
		get: function(a, b) {
			return bq.test((b && a.currentStyle ? a.currentStyle.filter: a.style.filter) || "") ? parseFloat(RegExp.$1) / 100 + "": b ? "1": ""
		},
		set: function(a, b) {
			var c = a.style,
			d = a.currentStyle,
			e = f.isNumeric(b) ? "alpha(opacity=" + b * 100 + ")": "",
			g = d && d.filter || c.filter || "";
			c.zoom = 1;
			if (b >= 1 && f.trim(g.replace(bp, "")) === "") {
				c.removeAttribute("filter");
				if (d && !d.filter) {
					return
				}
			}
			c.filter = bp.test(g) ? g.replace(bp, e) : g + " " + e
		}
	}),
	f(function() {
		f.support.reliableMarginRight || (f.cssHooks.marginRight = {
			get: function(a, b) {
				return f.swap(a, {
					display: "inline-block"
				},
				function() {
					return b ? by(a, "margin-right") : a.style.marginRight
				})
			}
		})
	}),
	f.expr && f.expr.filters && (f.expr.filters.hidden = function(a) {
		var b = a.offsetWidth,
		c = a.offsetHeight;
		return b === 0 && c === 0 || !f.support.reliableHiddenOffsets && (a.style && a.style.display || f.css(a, "display")) === "none"
	},
	f.expr.filters.visible = function(a) {
		return ! f.expr.filters.hidden(a)
	}),
	f.each({
		margin: "",
		padding: "",
		border: "Width"
	},
	function(a, b) {
		f.cssHooks[a + b] = {
			expand: function(c) {
				var d, e = typeof c == "string" ? c.split(" ") : [c],
				f = {};
				for (d = 0; d < 4; d++) {
					f[a + bx[d] + b] = e[d] || e[d - 2] || e[0]
				}
				return f
			}
		}
	});
	var bC = /%20/g,
	bD = /\[\]$/,
	bE = /\r?\n/g,
	bF = /#.*$/,
	bG = /^(.*?):[ \t]*([^\r\n]*)\r?$/mg,
	bH = /^(?:color|date|datetime|datetime-local|email|hidden|month|number|password|range|search|tel|text|time|url|week)$/i,
	bI = /^(?:about|app|app\-storage|.+\-extension|file|res|widget):$/,
	bJ = /^(?:GET|HEAD)$/,
	bK = /^\/\//,
	bL = /\?/,
	bM = /<script\b[^<]*(?:(?!<\/script>)<[^<]*)*<\/script>/gi,
	bN = /^(?:select|textarea)/i,
	bO = /\s+/,
	bP = /([?&])_=[^&]*/,
	bQ = /^([\w\+\.\-]+:)(?:\/\/([^\/?#:]*)(?::(\d+))?)?/,
	bR = f.fn.load,
	bS = {},
	bT = {},
	bU, bV, bW = ["*/"] + ["*"];
	try {
		bU = e.href
	} catch(bX) {
		bU = c.createElement("a"),
		bU.href = "",
		bU = bU.href
	}
	bV = bQ.exec(bU.toLowerCase()) || [],
	f.fn.extend({
		load: function(a, c, d) {
			if (typeof a != "string" && bR) {
				return bR.apply(this, arguments)
			}
			if (!this.length) {
				return this
			}
			var e = a.indexOf(" ");
			if (e >= 0) {
				var g = a.slice(e, a.length);
				a = a.slice(0, e)
			}
			var h = "GET";
			c && (f.isFunction(c) ? (d = c, c = b) : typeof c == "object" && (c = f.param(c, f.ajaxSettings.traditional), h = "POST"));
			var i = this;
			f.ajax({
				url: a,
				type: h,
				dataType: "html",
				data: c,
				complete: function(a, b, c) {
					c = a.responseText,
					a.isResolved() && (a.done(function(a) {
						c = a
					}), i.html(g ? f("<div>").append(c.replace(bM, "")).find(g) : c)),
					d && i.each(d, [c, b, a])
				}
			});
			return this
		},
		serialize: function() {
			return f.param(this.serializeArray())
		},
		serializeArray: function() {
			return this.map(function() {
				return this.elements ? f.makeArray(this.elements) : this
			}).filter(function() {
				return this.name && !this.disabled && (this.checked || bN.test(this.nodeName) || bH.test(this.type))
			}).map(function(a, b) {
				var c = f(this).val();
				return c == null ? null: f.isArray(c) ? f.map(c,
				function(a, c) {
					return {
						name: b.name,
						value: a.replace(bE, "\r\n")
					}
				}) : {
					name: b.name,
					value: c.replace(bE, "\r\n")
				}
			}).get()
		}
	}),
	f.each("ajaxStart ajaxStop ajaxComplete ajaxError ajaxSuccess ajaxSend".split(" "),
	function(a, b) {
		f.fn[b] = function(a) {
			return this.on(b, a)
		}
	}),
	f.each(["get", "post"],
	function(a, c) {
		f[c] = function(a, d, e, g) {
			f.isFunction(d) && (g = g || e, e = d, d = b);
			return f.ajax({
				type: c,
				url: a,
				data: d,
				success: e,
				dataType: g
			})
		}
	}),
	f.extend({
		getScript: function(a, c) {
			return f.get(a, b, c, "script")
		},
		getJSON: function(a, b, c) {
			return f.get(a, b, c, "json")
		},
		ajaxSetup: function(a, b) {
			b ? b$(a, f.ajaxSettings) : (b = a, a = f.ajaxSettings),
			b$(a, b);
			return a
		},
		ajaxSettings: {
			url: bU,
			isLocal: bI.test(bV[1]),
			global: !0,
			type: "GET",
			contentType: "application/x-www-form-urlencoded; charset=UTF-8",
			processData: !0,
			async: !0,
			accepts: {
				xml: "application/xml, text/xml",
				html: "text/html",
				text: "text/plain",
				json: "application/json, text/javascript",
				"*": bW
			},
			contents: {
				xml: /xml/,
				html: /html/,
				json: /json/
			},
			responseFields: {
				xml: "responseXML",
				text: "responseText"
			},
			converters: {
				"* text": a.String,
				"text html": !0,
				"text json": f.parseJSON,
				"text xml": f.parseXML
			},
			flatOptions: {
				context: !0,
				url: !0
			}
		},
		ajaxPrefilter: bY(bS),
		ajaxTransport: bY(bT),
		ajax: function(a, c) {
			function w(a, c, l, m) {
				if (s !== 2) {
					s = 2,
					q && clearTimeout(q),
					p = b,
					n = m || "",
					v.readyState = a > 0 ? 4 : 0;
					var o, r, u, w = c,
					x = l ? ca(d, v, l) : b,
					y,
					z;
					if (a >= 200 && a < 300 || a === 304) {
						if (d.ifModified) {
							if (y = v.getResponseHeader("Last-Modified")) {
								f.lastModified[k] = y
							}
							if (z = v.getResponseHeader("Etag")) {
								f.etag[k] = z
							}
						}
						if (a === 304) {
							w = "notmodified",
							o = !0
						} else {
							try {
								r = cb(d, x),
								w = "success",
								o = !0
							} catch(A) {
								w = "parsererror",
								u = A
							}
						}
					} else {
						u = w;
						if (!w || a) {
							w = "error",
							a < 0 && (a = 0)
						}
					}
					v.status = a,
					v.statusText = "" + (c || w),
					o ? h.resolveWith(e, [r, w, v]) : h.rejectWith(e, [v, w, u]),
					v.statusCode(j),
					j = b,
					t && g.trigger("ajax" + (o ? "Success": "Error"), [v, d, o ? r: u]),
					i.fireWith(e, [v, w]),
					t && (g.trigger("ajaxComplete", [v, d]), --f.active || f.event.trigger("ajaxStop"))
				}
			}
			typeof a == "object" && (c = a, a = b),
			c = c || {};
			var d = f.ajaxSetup({},
			c),
			e = d.context || d,
			g = e !== d && (e.nodeType || e instanceof f) ? f(e) : f.event,
			h = f.Deferred(),
			i = f.Callbacks("once memory"),
			j = d.statusCode || {},
			k,
			l = {},
			m = {},
			n,
			o,
			p,
			q,
			r,
			s = 0,
			t,
			u,
			v = {
				readyState: 0,
				setRequestHeader: function(a, b) {
					if (!s) {
						var c = a.toLowerCase();
						a = m[c] = m[c] || a,
						l[a] = b
					}
					return this
				},
				getAllResponseHeaders: function() {
					return s === 2 ? n: null
				},
				getResponseHeader: function(a) {
					var c;
					if (s === 2) {
						if (!o) {
							o = {};
							while (c = bG.exec(n)) {
								o[c[1].toLowerCase()] = c[2]
							}
						}
						c = o[a.toLowerCase()]
					}
					return c === b ? null: c
				},
				overrideMimeType: function(a) {
					s || (d.mimeType = a);
					return this
				},
				abort: function(a) {
					a = a || "abort",
					p && p.abort(a),
					w(0, a);
					return this
				}
			};
			h.promise(v),
			v.success = v.done,
			v.error = v.fail,
			v.complete = i.add,
			v.statusCode = function(a) {
				if (a) {
					var b;
					if (s < 2) {
						for (b in a) {
							j[b] = [j[b], a[b]]
						}
					} else {
						b = a[v.status],
						v.then(b, b)
					}
				}
				return this
			},
			d.url = ((a || d.url) + "").replace(bF, "").replace(bK, bV[1] + "//"),
			d.dataTypes = f.trim(d.dataType || "*").toLowerCase().split(bO),
			d.crossDomain == null && (r = bQ.exec(d.url.toLowerCase()), d.crossDomain = !(!r || r[1] == bV[1] && r[2] == bV[2] && (r[3] || (r[1] === "http:" ? 80 : 443)) == (bV[3] || (bV[1] === "http:" ? 80 : 443)))),
			d.data && d.processData && typeof d.data != "string" && (d.data = f.param(d.data, d.traditional)),
			bZ(bS, d, c, v);
			if (s === 2) {
				return ! 1
			}
			t = d.global,
			d.type = d.type.toUpperCase(),
			d.hasContent = !bJ.test(d.type),
			t && f.active++===0 && f.event.trigger("ajaxStart");
			if (!d.hasContent) {
				d.data && (d.url += (bL.test(d.url) ? "&": "?") + d.data, delete d.data),
				k = d.url;
				if (d.cache === !1) {
					var x = f.now(),
					y = d.url.replace(bP, "$1_=" + x);
					d.url = y + (y === d.url ? (bL.test(d.url) ? "&": "?") + "_=" + x: "")
				}
			} (d.data && d.hasContent && d.contentType !== !1 || c.contentType) && v.setRequestHeader("Content-Type", d.contentType),
			d.ifModified && (k = k || d.url, f.lastModified[k] && v.setRequestHeader("If-Modified-Since", f.lastModified[k]), f.etag[k] && v.setRequestHeader("If-None-Match", f.etag[k])),
			v.setRequestHeader("Accept", d.dataTypes[0] && d.accepts[d.dataTypes[0]] ? d.accepts[d.dataTypes[0]] + (d.dataTypes[0] !== "*" ? ", " + bW + "; q=0.01": "") : d.accepts["*"]);
			for (u in d.headers) {
				v.setRequestHeader(u, d.headers[u])
			}
			if (d.beforeSend && (d.beforeSend.call(e, v, d) === !1 || s === 2)) {
				v.abort();
				return ! 1
			}
			for (u in {
				success: 1,
				error: 1,
				complete: 1
			}) {
				v[u](d[u])
			}
			p = bZ(bT, d, c, v);
			if (!p) {
				w( - 1, "No Transport")
			} else {
				v.readyState = 1,
				t && g.trigger("ajaxSend", [v, d]),
				d.async && d.timeout > 0 && (q = setTimeout(function() {
					v.abort("timeout")
				},
				d.timeout));
				try {
					s = 1,
					p.send(l, w)
				} catch(z) {
					if (s < 2) {
						w( - 1, z)
					} else {
						throw z
					}
				}
			}
			return v
		},
		param: function(a, c) {
			var d = [],
			e = function(a, b) {
				b = f.isFunction(b) ? b() : b,
				d[d.length] = encodeURIComponent(a) + "=" + encodeURIComponent(b)
			};
			c === b && (c = f.ajaxSettings.traditional);
			if (f.isArray(a) || a.jquery && !f.isPlainObject(a)) {
				f.each(a,
				function() {
					e(this.name, this.value)
				})
			} else {
				for (var g in a) {
					b_(g, a[g], c, e)
				}
			}
			return d.join("&").replace(bC, "+")
		}
	}),
	f.extend({
		active: 0,
		lastModified: {},
		etag: {}
	});
	var cc = f.now(),
	cd = /(\=)\?(&|$)|\?\?/i;
	f.ajaxSetup({
		jsonp: "callback",
		jsonpCallback: function() {
			return f.expando + "_" + cc++
		}
	}),
	f.ajaxPrefilter("json jsonp",
	function(b, c, d) {
		var e = typeof b.data == "string" && /^application\/x\-www\-form\-urlencoded/.test(b.contentType);
		if (b.dataTypes[0] === "jsonp" || b.jsonp !== !1 && (cd.test(b.url) || e && cd.test(b.data))) {
			var g, h = b.jsonpCallback = f.isFunction(b.jsonpCallback) ? b.jsonpCallback() : b.jsonpCallback,
			i = a[h],
			j = b.url,
			k = b.data,
			l = "$1" + h + "$2";
			b.jsonp !== !1 && (j = j.replace(cd, l), b.url === j && (e && (k = k.replace(cd, l)), b.data === k && (j += (/\?/.test(j) ? "&": "?") + b.jsonp + "=" + h))),
			b.url = j,
			b.data = k,
			a[h] = function(a) {
				g = [a]
			},
			d.always(function() {
				a[h] = i,
				g && f.isFunction(i) && a[h](g[0])
			}),
			b.converters["script json"] = function() {
				g || f.error(h + " was not called");
				return g[0]
			},
			b.dataTypes[0] = "json";
			return "script"
		}
	}),
	f.ajaxSetup({
		accepts: {
			script: "text/javascript, application/javascript, application/ecmascript, application/x-ecmascript"
		},
		contents: {
			script: /javascript|ecmascript/
		},
		converters: {
			"text script": function(a) {
				f.globalEval(a);
				return a
			}
		}
	}),
	f.ajaxPrefilter("script",
	function(a) {
		a.cache === b && (a.cache = !1),
		a.crossDomain && (a.type = "GET", a.global = !1)
	}),
	f.ajaxTransport("script",
	function(a) {
		if (a.crossDomain) {
			var d, e = c.head || c.getElementsByTagName("head")[0] || c.documentElement;
			return {
				send: function(f, g) {
					d = c.createElement("script"),
					d.async = "async",
					a.scriptCharset && (d.charset = a.scriptCharset),
					d.src = a.url,
					d.onload = d.onreadystatechange = function(a, c) {
						if (c || !d.readyState || /loaded|complete/.test(d.readyState)) {
							d.onload = d.onreadystatechange = null,
							e && d.parentNode && e.removeChild(d),
							d = b,
							c || g(200, "success")
						}
					},
					e.insertBefore(d, e.firstChild)
				},
				abort: function() {
					d && d.onload(0, 1)
				}
			}
		}
	});
	var ce = a.ActiveXObject ?
	function() {
		for (var a in cg) {
			cg[a](0, 1)
		}
	}: !1,
	cf = 0,
	cg;
	f.ajaxSettings.xhr = a.ActiveXObject ?
	function() {
		return ! this.isLocal && ch() || ci()
	}: ch,
	function(a) {
		f.extend(f.support, {
			ajax: !!a,
			cors: !!a && "withCredentials" in a
		})
	} (f.ajaxSettings.xhr()),
	f.support.ajax && f.ajaxTransport(function(c) {
		if (!c.crossDomain || f.support.cors) {
			var d;
			return {
				send: function(e, g) {
					var h = c.xhr(),
					i,
					j;
					c.username ? h.open(c.type, c.url, c.async, c.username, c.password) : h.open(c.type, c.url, c.async);
					if (c.xhrFields) {
						for (j in c.xhrFields) {
							h[j] = c.xhrFields[j]
						}
					}
					c.mimeType && h.overrideMimeType && h.overrideMimeType(c.mimeType),
					!c.crossDomain && !e["X-Requested-With"] && (e["X-Requested-With"] = "XMLHttpRequest");
					try {
						for (j in e) {
							h.setRequestHeader(j, e[j])
						}
					} catch(k) {}
					h.send(c.hasContent && c.data || null),
					d = function(a, e) {
						var j, k, l, m, n;
						try {
							if (d && (e || h.readyState === 4)) {
								d = b,
								i && (h.onreadystatechange = f.noop, ce && delete cg[i]);
								if (e) {
									h.readyState !== 4 && h.abort()
								} else {
									j = h.status,
									l = h.getAllResponseHeaders(),
									m = {},
									n = h.responseXML,
									n && n.documentElement && (m.xml = n);
									try {
										m.text = h.responseText
									} catch(a) {}
									try {
										k = h.statusText
									} catch(o) {
										k = ""
									} ! j && c.isLocal && !c.crossDomain ? j = m.text ? 200 : 404 : j === 1223 && (j = 204)
								}
							}
						} catch(p) {
							e || g( - 1, p)
						}
						m && g(j, k, m, l)
					},
					!c.async || h.readyState === 4 ? d() : (i = ++cf, ce && (cg || (cg = {},
					f(a).unload(ce)), cg[i] = d), h.onreadystatechange = d)
				},
				abort: function() {
					d && d(0, 1)
				}
			}
		}
	});
	var cj = {},
	ck, cl, cm = /^(?:toggle|show|hide)$/,
	cn = /^([+\-]=)?([\d+.\-]+)([a-z%]*)$/i,
	co, cp = [["height", "marginTop", "marginBottom", "paddingTop", "paddingBottom"], ["width", "marginLeft", "marginRight", "paddingLeft", "paddingRight"], ["opacity"]],
	cq;
	f.fn.extend({
		show: function(a, b, c) {
			var d, e;
			if (a || a === 0) {
				return this.animate(ct("show", 3), a, b, c)
			}
			for (var g = 0,
			h = this.length; g < h; g++) {
				d = this[g],
				d.style && (e = d.style.display, !f._data(d, "olddisplay") && e === "none" && (e = d.style.display = ""), (e === "" && f.css(d, "display") === "none" || !f.contains(d.ownerDocument.documentElement, d)) && f._data(d, "olddisplay", cu(d.nodeName)))
			}
			for (g = 0; g < h; g++) {
				d = this[g];
				if (d.style) {
					e = d.style.display;
					if (e === "" || e === "none") {
						d.style.display = f._data(d, "olddisplay") || ""
					}
				}
			}
			return this
		},
		hide: function(a, b, c) {
			if (a || a === 0) {
				return this.animate(ct("hide", 3), a, b, c)
			}
			var d, e, g = 0,
			h = this.length;
			for (; g < h; g++) {
				d = this[g],
				d.style && (e = f.css(d, "display"), e !== "none" && !f._data(d, "olddisplay") && f._data(d, "olddisplay", e))
			}
			for (g = 0; g < h; g++) {
				this[g].style && (this[g].style.display = "none")
			}
			return this
		},
		_toggle: f.fn.toggle,
		toggle: function(a, b, c) {
			var d = typeof a == "boolean";
			f.isFunction(a) && f.isFunction(b) ? this._toggle.apply(this, arguments) : a == null || d ? this.each(function() {
				var b = d ? a: f(this).is(":hidden");
				f(this)[b ? "show": "hide"]()
			}) : this.animate(ct("toggle", 3), a, b, c);
			return this
		},
		fadeTo: function(a, b, c, d) {
			return this.filter(":hidden").css("opacity", 0).show().end().animate({
				opacity: b
			},
			a, c, d)
		},
		animate: function(a, b, c, d) {
			function g() {
				e.queue === !1 && f._mark(this);
				var b = f.extend({},
				e),
				c = this.nodeType === 1,
				d = c && f(this).is(":hidden"),
				g,
				h,
				i,
				j,
				k,
				l,
				m,
				n,
				o,
				p,
				q;
				b.animatedProperties = {};
				for (i in a) {
					g = f.camelCase(i),
					i !== g && (a[g] = a[i], delete a[i]);
					if ((k = f.cssHooks[g]) && "expand" in k) {
						l = k.expand(a[g]),
						delete a[g];
						for (i in l) {
							i in a || (a[i] = l[i])
						}
					}
				}
				for (g in a) {
					h = a[g],
					f.isArray(h) ? (b.animatedProperties[g] = h[1], h = a[g] = h[0]) : b.animatedProperties[g] = b.specialEasing && b.specialEasing[g] || b.easing || "swing";
					if (h === "hide" && d || h === "show" && !d) {
						return b.complete.call(this)
					}
					c && (g === "height" || g === "width") && (b.overflow = [this.style.overflow, this.style.overflowX, this.style.overflowY], f.css(this, "display") === "inline" && f.css(this, "float") === "none" && (!f.support.inlineBlockNeedsLayout || cu(this.nodeName) === "inline" ? this.style.display = "inline-block": this.style.zoom = 1))
				}
				b.overflow != null && (this.style.overflow = "hidden");
				for (i in a) {
					j = new f.fx(this, b, i),
					h = a[i],
					cm.test(h) ? (q = f._data(this, "toggle" + i) || (h === "toggle" ? d ? "show": "hide": 0), q ? (f._data(this, "toggle" + i, q === "show" ? "hide": "show"), j[q]()) : j[h]()) : (m = cn.exec(h), n = j.cur(), m ? (o = parseFloat(m[2]), p = m[3] || (f.cssNumber[i] ? "": "px"), p !== "px" && (f.style(this, i, (o || 1) + p), n = (o || 1) / j.cur() * n, f.style(this, i, n + p)), m[1] && (o = (m[1] === "-=" ? -1 : 1) * o + n), j.custom(n, o, p)) : j.custom(n, h, ""))
				}
				return ! 0
			}
			var e = f.speed(b, c, d);
			if (f.isEmptyObject(a)) {
				return this.each(e.complete, [!1])
			}
			a = f.extend({},
			a);
			return e.queue === !1 ? this.each(g) : this.queue(e.queue, g)
		},
		stop: function(a, c, d) {
			typeof a != "string" && (d = c, c = a, a = b),
			c && a !== !1 && this.queue(a || "fx", []);
			return this.each(function() {
				function h(a, b, c) {
					var e = b[c];
					f.removeData(a, c, !0),
					e.stop(d)
				}
				var b, c = !1,
				e = f.timers,
				g = f._data(this);
				d || f._unmark(!0, this);
				if (a == null) {
					for (b in g) {
						g[b] && g[b].stop && b.indexOf(".run") === b.length - 4 && h(this, g, b)
					}
				} else {
					g[b = a + ".run"] && g[b].stop && h(this, g, b)
				}
				for (b = e.length; b--;) {
					e[b].elem === this && (a == null || e[b].queue === a) && (d ? e[b](!0) : e[b].saveState(), c = !0, e.splice(b, 1))
				} (!d || !c) && f.dequeue(this, a)
			})
		}
	}),
	f.each({
		slideDown: ct("show", 1),
		slideUp: ct("hide", 1),
		slideToggle: ct("toggle", 1),
		fadeIn: {
			opacity: "show"
		},
		fadeOut: {
			opacity: "hide"
		},
		fadeToggle: {
			opacity: "toggle"
		}
	},
	function(a, b) {
		f.fn[a] = function(a, c, d) {
			return this.animate(b, a, c, d)
		}
	}),
	f.extend({
		speed: function(a, b, c) {
			var d = a && typeof a == "object" ? f.extend({},
			a) : {
				complete: c || !c && b || f.isFunction(a) && a,
				duration: a,
				easing: c && b || b && !f.isFunction(b) && b
			};
			d.duration = f.fx.off ? 0 : typeof d.duration == "number" ? d.duration: d.duration in f.fx.speeds ? f.fx.speeds[d.duration] : f.fx.speeds._default;
			if (d.queue == null || d.queue === !0) {
				d.queue = "fx"
			}
			d.old = d.complete,
			d.complete = function(a) {
				f.isFunction(d.old) && d.old.call(this),
				d.queue ? f.dequeue(this, d.queue) : a !== !1 && f._unmark(this)
			};
			return d
		},
		easing: {
			linear: function(a) {
				return a
			},
			swing: function(a) {
				return - Math.cos(a * Math.PI) / 2 + 0.5
			}
		},
		timers: [],
		fx: function(a, b, c) {
			this.options = b,
			this.elem = a,
			this.prop = c,
			b.orig = b.orig || {}
		}
	}),
	f.fx.prototype = {
		update: function() {
			this.options.step && this.options.step.call(this.elem, this.now, this),
			(f.fx.step[this.prop] || f.fx.step._default)(this)
		},
		cur: function() {
			if (this.elem[this.prop] != null && (!this.elem.style || this.elem.style[this.prop] == null)) {
				return this.elem[this.prop]
			}
			var a, b = f.css(this.elem, this.prop);
			return isNaN(a = parseFloat(b)) ? !b || b === "auto" ? 0 : b: a
		},
		custom: function(a, c, d) {
			function h(a) {
				return e.step(a)
			}
			var e = this,
			g = f.fx;
			this.startTime = cq || cr(),
			this.end = c,
			this.now = this.start = a,
			this.pos = this.state = 0,
			this.unit = d || this.unit || (f.cssNumber[this.prop] ? "": "px"),
			h.queue = this.options.queue,
			h.elem = this.elem,
			h.saveState = function() {
				f._data(e.elem, "fxshow" + e.prop) === b && (e.options.hide ? f._data(e.elem, "fxshow" + e.prop, e.start) : e.options.show && f._data(e.elem, "fxshow" + e.prop, e.end))
			},
			h() && f.timers.push(h) && !co && (co = setInterval(g.tick, g.interval))
		},
		show: function() {
			var a = f._data(this.elem, "fxshow" + this.prop);
			this.options.orig[this.prop] = a || f.style(this.elem, this.prop),
			this.options.show = !0,
			a !== b ? this.custom(this.cur(), a) : this.custom(this.prop === "width" || this.prop === "height" ? 1 : 0, this.cur()),
			f(this.elem).show()
		},
		hide: function() {
			this.options.orig[this.prop] = f._data(this.elem, "fxshow" + this.prop) || f.style(this.elem, this.prop),
			this.options.hide = !0,
			this.custom(this.cur(), 0)
		},
		step: function(a) {
			var b, c, d, e = cq || cr(),
			g = !0,
			h = this.elem,
			i = this.options;
			if (a || e >= i.duration + this.startTime) {
				this.now = this.end,
				this.pos = this.state = 1,
				this.update(),
				i.animatedProperties[this.prop] = !0;
				for (b in i.animatedProperties) {
					i.animatedProperties[b] !== !0 && (g = !1)
				}
				if (g) {
					i.overflow != null && !f.support.shrinkWrapBlocks && f.each(["", "X", "Y"],
					function(a, b) {
						h.style["overflow" + b] = i.overflow[a]
					}),
					i.hide && f(h).hide();
					if (i.hide || i.show) {
						for (b in i.animatedProperties) {
							f.style(h, b, i.orig[b]),
							f.removeData(h, "fxshow" + b, !0),
							f.removeData(h, "toggle" + b, !0)
						}
					}
					d = i.complete,
					d && (i.complete = !1, d.call(h))
				}
				return ! 1
			}
			i.duration == Infinity ? this.now = e: (c = e - this.startTime, this.state = c / i.duration, this.pos = f.easing[i.animatedProperties[this.prop]](this.state, c, 0, 1, i.duration), this.now = this.start + (this.end - this.start) * this.pos),
			this.update();
			return ! 0
		}
	},
	f.extend(f.fx, {
		tick: function() {
			var a, b = f.timers,
			c = 0;
			for (; c < b.length; c++) {
				a = b[c],
				!a() && b[c] === a && b.splice(c--, 1)
			}
			b.length || f.fx.stop()
		},
		interval: 13,
		stop: function() {
			clearInterval(co),
			co = null
		},
		speeds: {
			slow: 600,
			fast: 200,
			_default: 400
		},
		step: {
			opacity: function(a) {
				f.style(a.elem, "opacity", a.now)
			},
			_default: function(a) {
				a.elem.style && a.elem.style[a.prop] != null ? a.elem.style[a.prop] = a.now + a.unit: a.elem[a.prop] = a.now
			}
		}
	}),
	f.each(cp.concat.apply([], cp),
	function(a, b) {
		b.indexOf("margin") && (f.fx.step[b] = function(a) {
			f.style(a.elem, b, Math.max(0, a.now) + a.unit)
		})
	}),
	f.expr && f.expr.filters && (f.expr.filters.animated = function(a) {
		return f.grep(f.timers,
		function(b) {
			return a === b.elem
		}).length
	});
	var cv, cw = /^t(?:able|d|h)$/i,
	cx = /^(?:body|html)$/i;
	"getBoundingClientRect" in c.documentElement ? cv = function(a, b, c, d) {
		try {
			d = a.getBoundingClientRect()
		} catch(e) {}
		if (!d || !f.contains(c, a)) {
			return d ? {
				top: d.top,
				left: d.left
			}: {
				top: 0,
				left: 0
			}
		}
		var g = b.body,
		h = cy(b),
		i = c.clientTop || g.clientTop || 0,
		j = c.clientLeft || g.clientLeft || 0,
		k = h.pageYOffset || f.support.boxModel && c.scrollTop || g.scrollTop,
		l = h.pageXOffset || f.support.boxModel && c.scrollLeft || g.scrollLeft,
		m = d.top + k - i,
		n = d.left + l - j;
		return {
			top: m,
			left: n
		}
	}: cv = function(a, b, c) {
		var d, e = a.offsetParent,
		g = a,
		h = b.body,
		i = b.defaultView,
		j = i ? i.getComputedStyle(a, null) : a.currentStyle,
		k = a.offsetTop,
		l = a.offsetLeft;
		while ((a = a.parentNode) && a !== h && a !== c) {
			if (f.support.fixedPosition && j.position === "fixed") {
				break
			}
			d = i ? i.getComputedStyle(a, null) : a.currentStyle,
			k -= a.scrollTop,
			l -= a.scrollLeft,
			a === e && (k += a.offsetTop, l += a.offsetLeft, f.support.doesNotAddBorder && (!f.support.doesAddBorderForTableAndCells || !cw.test(a.nodeName)) && (k += parseFloat(d.borderTopWidth) || 0, l += parseFloat(d.borderLeftWidth) || 0), g = e, e = a.offsetParent),
			f.support.subtractsBorderForOverflowNotVisible && d.overflow !== "visible" && (k += parseFloat(d.borderTopWidth) || 0, l += parseFloat(d.borderLeftWidth) || 0),
			j = d
		}
		if (j.position === "relative" || j.position === "static") {
			k += h.offsetTop,
			l += h.offsetLeft
		}
		f.support.fixedPosition && j.position === "fixed" && (k += Math.max(c.scrollTop, h.scrollTop), l += Math.max(c.scrollLeft, h.scrollLeft));
		return {
			top: k,
			left: l
		}
	},
	f.fn.offset = function(a) {
		if (arguments.length) {
			return a === b ? this: this.each(function(b) {
				f.offset.setOffset(this, a, b)
			})
		}
		var c = this[0],
		d = c && c.ownerDocument;
		if (!d) {
			return null
		}
		if (c === d.body) {
			return f.offset.bodyOffset(c)
		}
		return cv(c, d, d.documentElement)
	},
	f.offset = {
		bodyOffset: function(a) {
			var b = a.offsetTop,
			c = a.offsetLeft;
			f.support.doesNotIncludeMarginInBodyOffset && (b += parseFloat(f.css(a, "marginTop")) || 0, c += parseFloat(f.css(a, "marginLeft")) || 0);
			return {
				top: b,
				left: c
			}
		},
		setOffset: function(a, b, c) {
			var d = f.css(a, "position");
			d === "static" && (a.style.position = "relative");
			var e = f(a),
			g = e.offset(),
			h = f.css(a, "top"),
			i = f.css(a, "left"),
			j = (d === "absolute" || d === "fixed") && f.inArray("auto", [h, i]) > -1,
			k = {},
			l = {},
			m,
			n;
			j ? (l = e.position(), m = l.top, n = l.left) : (m = parseFloat(h) || 0, n = parseFloat(i) || 0),
			f.isFunction(b) && (b = b.call(a, c, g)),
			b.top != null && (k.top = b.top - g.top + m),
			b.left != null && (k.left = b.left - g.left + n),
			"using" in b ? b.using.call(a, k) : e.css(k)
		}
	},
	f.fn.extend({
		position: function() {
			if (!this[0]) {
				return null
			}
			var a = this[0],
			b = this.offsetParent(),
			c = this.offset(),
			d = cx.test(b[0].nodeName) ? {
				top: 0,
				left: 0
			}: b.offset();
			c.top -= parseFloat(f.css(a, "marginTop")) || 0,
			c.left -= parseFloat(f.css(a, "marginLeft")) || 0,
			d.top += parseFloat(f.css(b[0], "borderTopWidth")) || 0,
			d.left += parseFloat(f.css(b[0], "borderLeftWidth")) || 0;
			return {
				top: c.top - d.top,
				left: c.left - d.left
			}
		},
		offsetParent: function() {
			return this.map(function() {
				var a = this.offsetParent || c.body;
				while (a && !cx.test(a.nodeName) && f.css(a, "position") === "static") {
					a = a.offsetParent
				}
				return a
			})
		}
	}),
	f.each({
		scrollLeft: "pageXOffset",
		scrollTop: "pageYOffset"
	},
	function(a, c) {
		var d = /Y/.test(c);
		f.fn[a] = function(e) {
			return f.access(this,
			function(a, e, g) {
				var h = cy(a);
				if (g === b) {
					return h ? c in h ? h[c] : f.support.boxModel && h.document.documentElement[e] || h.document.body[e] : a[e]
				}
				h ? h.scrollTo(d ? f(h).scrollLeft() : g, d ? g: f(h).scrollTop()) : a[e] = g
			},
			a, e, arguments.length, null)
		}
	}),
	f.each({
		Height: "height",
		Width: "width"
	},
	function(a, c) {
		var d = "client" + a,
		e = "scroll" + a,
		g = "offset" + a;
		f.fn["inner" + a] = function() {
			var a = this[0];
			return a ? a.style ? parseFloat(f.css(a, c, "padding")) : this[c]() : null
		},
		f.fn["outer" + a] = function(a) {
			var b = this[0];
			return b ? b.style ? parseFloat(f.css(b, c, a ? "margin": "border")) : this[c]() : null
		},
		f.fn[c] = function(a) {
			return f.access(this,
			function(a, c, h) {
				var i, j, k, l;
				if (f.isWindow(a)) {
					i = a.document,
					j = i.documentElement[d];
					return f.support.boxModel && j || i.body && i.body[d] || j
				}
				if (a.nodeType === 9) {
					i = a.documentElement;
					if (i[d] >= i[e]) {
						return i[d]
					}
					return Math.max(a.body[e], i[e], a.body[g], i[g])
				}
				if (h === b) {
					k = f.css(a, c),
					l = parseFloat(k);
					return f.isNumeric(l) ? l: k
				}
				f(a).css(c, h)
			},
			c, a, arguments.length, null)
		}
	}),
	a.jQuery = a.$ = f,
	typeof define == "function" && define.amd && define.amd.jQuery && define("jquery", [],
	function() {
		return f
	})
})(window);

jQuery.ui || (function(c) {
	var i = c.fn.remove,
	d = c.browser.mozilla && (parseFloat(c.browser.version) < 1.9);
	c.ui = {
		version: "1.7.2",
		plugin: {
			add: function(k, l, n) {
				var m = c.ui[k].prototype;
				for (var j in n) {
					m.plugins[j] = m.plugins[j] || [];
					m.plugins[j].push([l, n[j]])
				}
			},
			call: function(j, l, k) {
				var n = j.plugins[l];
				if (!n || !j.element[0].parentNode) {
					return
				}
				for (var m = 0; m < n.length; m++) {
					if (j.options[n[m][0]]) {
						n[m][1].apply(j.element, k)
					}
				}
			}
		},
		contains: function(k, j) {
			return document.compareDocumentPosition ? k.compareDocumentPosition(j) & 16 : k !== j && k.contains(j)
		},
		hasScroll: function(m, k) {
			if (c(m).css("overflow") == "hidden") {
				return false
			}
			var j = (k && k == "left") ? "scrollLeft": "scrollTop",
			l = false;
			if (m[j] > 0) {
				return true
			}
			m[j] = 1;
			l = (m[j] > 0);
			m[j] = 0;
			return l
		},
		isOverAxis: function(k, j, l) {
			return (k > j) && (k < (j + l))
		},
		isOver: function(o, k, n, m, j, l) {
			return c.ui.isOverAxis(o, n, j) && c.ui.isOverAxis(k, m, l)
		},
		keyCode: {
			BACKSPACE: 8,
			CAPS_LOCK: 20,
			COMMA: 188,
			CONTROL: 17,
			DELETE: 46,
			DOWN: 40,
			END: 35,
			ENTER: 13,
			ESCAPE: 27,
			HOME: 36,
			INSERT: 45,
			LEFT: 37,
			NUMPAD_ADD: 107,
			NUMPAD_DECIMAL: 110,
			NUMPAD_DIVIDE: 111,
			NUMPAD_ENTER: 108,
			NUMPAD_MULTIPLY: 106,
			NUMPAD_SUBTRACT: 109,
			PAGE_DOWN: 34,
			PAGE_UP: 33,
			PERIOD: 190,
			RIGHT: 39,
			SHIFT: 16,
			SPACE: 32,
			TAB: 9,
			UP: 38
		}
	};
	if (d) {
		var f = c.attr,
		e = c.fn.removeAttr,
		h = "http://www.w3.org/2005/07/aaa",
		a = /^aria-/,
		b = /^wairole:/;
		c.attr = function(k, j, l) {
			var m = l !== undefined;
			return (j == "role" ? (m ? f.call(this, k, j, "wairole:" + l) : (f.apply(this, arguments) || "").replace(b, "")) : (a.test(j) ? (m ? k.setAttributeNS(h, j.replace(a, "aaa:"), l) : f.call(this, k, j.replace(a, "aaa:"))) : f.apply(this, arguments)))
		};
		c.fn.removeAttr = function(j) {
			return (a.test(j) ? this.each(function() {
				this.removeAttributeNS(h, j.replace(a, ""))
			}) : e.call(this, j))
		}
	}
	c.fn.extend({
		remove: function() {
			c("*", this).add(this).each(function() {
				c(this).triggerHandler("remove")
			});
			return i.apply(this, arguments)
		},
		enableSelection: function() {
			return this.attr("unselectable", "off").css("MozUserSelect", "").unbind("selectstart.ui")
		},
		disableSelection: function() {
			return this.attr("unselectable", "on").css("MozUserSelect", "none").bind("selectstart.ui",
			function() {
				return false
			})
		},
		scrollParent: function() {
			var j;
			if ((c.browser.msie && (/(static|relative)/).test(this.css("position"))) || (/absolute/).test(this.css("position"))) {
				j = this.parents().filter(function() {
					return (/(relative|absolute|fixed)/).test(c.curCSS(this, "position", 1)) && (/(auto|scroll)/).test(c.curCSS(this, "overflow", 1) + c.curCSS(this, "overflow-y", 1) + c.curCSS(this, "overflow-x", 1))
				}).eq(0)
			} else {
				j = this.parents().filter(function() {
					return (/(auto|scroll)/).test(c.curCSS(this, "overflow", 1) + c.curCSS(this, "overflow-y", 1) + c.curCSS(this, "overflow-x", 1))
				}).eq(0)
			}
			return (/fixed/).test(this.css("position")) || !j.length ? c(document) : j
		}
	});
	c.extend(c.expr[":"], {
		data: function(l, k, j) {
			return !! c.data(l, j[3])
		},
		focusable: function(k) {
			var l = k.nodeName.toLowerCase(),
			j = c.attr(k, "tabindex");
			return (/input|select|textarea|button|object/.test(l) ? !k.disabled: "a" == l || "area" == l ? k.href || !isNaN(j) : !isNaN(j)) && !c(k)["area" == l ? "parents": "closest"](":hidden").length
		},
		tabbable: function(k) {
			var j = c.attr(k, "tabindex");
			return (isNaN(j) || j >= 0) && c(k).is(":focusable")
		}
	});
	function g(m, n, o, l) {
		function k(q) {
			var p = c[m][n][q] || [];
			return (typeof p == "string" ? p.split(/,?\s+/) : p)
		}
		var j = k("getter");
		if (l.length == 1 && typeof l[0] == "string") {
			j = j.concat(k("getterSetter"))
		}
		return (c.inArray(o, j) != -1)
	}
	c.widget = function(k, j) {
		var l = k.split(".")[0];
		k = k.split(".")[1];
		c.fn[k] = function(p) {
			var n = (typeof p == "string"),
			o = Array.prototype.slice.call(arguments, 1);
			if (n && p.substring(0, 1) == "_") {
				return this
			}
			if (n && g(l, k, p, o)) {
				var m = c.data(this[0], k);
				return (m ? m[p].apply(m, o) : undefined)
			}
			return this.each(function() {
				var q = c.data(this, k); (!q && !n && c.data(this, k, new c[l][k](this, p))._init()); (q && n && c.isFunction(q[p]) && q[p].apply(q, o))
			})
		};
		c[l] = c[l] || {};
		c[l][k] = function(o, n) {
			var m = this;
			this.namespace = l;
			this.widgetName = k;
			this.widgetEventPrefix = c[l][k].eventPrefix || k;
			this.widgetBaseClass = l + "-" + k;
			this.options = c.extend({},
			c.widget.defaults, c[l][k].defaults, c.metadata && c.metadata.get(o)[k], n);
			this.element = c(o).bind("setData." + k,
			function(q, p, r) {
				if (q.target == o) {
					return m._setData(p, r)
				}
			}).bind("getData." + k,
			function(q, p) {
				if (q.target == o) {
					return m._getData(p)
				}
			}).bind("remove",
			function() {
				return m.destroy()
			})
		};
		c[l][k].prototype = c.extend({},
		c.widget.prototype, j);
		c[l][k].getterSetter = "option"
	};
	c.widget.prototype = {
		_init: function() {},
		destroy: function() {
			this.element.removeData(this.widgetName).removeClass(this.widgetBaseClass + "-disabled " + this.namespace + "-state-disabled").removeAttr("aria-disabled")
		},
		option: function(l, m) {
			var k = l,
			j = this;
			if (typeof l == "string") {
				if (m === undefined) {
					return this._getData(l)
				}
				k = {};
				k[l] = m
			}
			c.each(k,
			function(n, o) {
				j._setData(n, o)
			})
		},
		_getData: function(j) {
			return this.options[j]
		},
		_setData: function(j, k) {
			this.options[j] = k;
			if (j == "disabled") {
				this.element[k ? "addClass": "removeClass"](this.widgetBaseClass + "-disabled " + this.namespace + "-state-disabled").attr("aria-disabled", k)
			}
		},
		enable: function() {
			this._setData("disabled", false)
		},
		disable: function() {
			this._setData("disabled", true)
		},
		_trigger: function(l, m, n) {
			var p = this.options[l],
			j = (l == this.widgetEventPrefix ? l: this.widgetEventPrefix + l);
			m = c.Event(m);
			m.type = j;
			if (m.originalEvent) {
				for (var k = c.event.props.length,
				o; k;) {
					o = c.event.props[--k];
					m[o] = m.originalEvent[o]
				}
			}
			this.element.trigger(m, n);
			return ! (c.isFunction(p) && p.call(this.element[0], m, n) === false || m.isDefaultPrevented())
		}
	};
	c.widget.defaults = {
		disabled: false
	};
	c.ui.mouse = {
		_mouseInit: function() {
			var j = this;
			this.element.bind("mousedown." + this.widgetName,
			function(k) {
				return j._mouseDown(k)
			}).bind("click." + this.widgetName,
			function(k) {
				if (j._preventClickEvent) {
					j._preventClickEvent = false;
					k.stopImmediatePropagation();
					return false
				}
			});
			if (c.browser.msie) {
				this._mouseUnselectable = this.element.attr("unselectable");
				this.element.attr("unselectable", "on")
			}
			this.started = false
		},
		_mouseDestroy: function() {
			this.element.unbind("." + this.widgetName); (c.browser.msie && this.element.attr("unselectable", this._mouseUnselectable))
		},
		_mouseDown: function(l) {
			l.originalEvent = l.originalEvent || {};
			if (l.originalEvent.mouseHandled) {
				return
			} (this._mouseStarted && this._mouseUp(l));
			this._mouseDownEvent = l;
			var k = this,
			m = (l.which == 1),
			j = (typeof this.options.cancel == "string" ? c(l.target).parents().add(l.target).filter(this.options.cancel).length: false);
			if (!m || j || !this._mouseCapture(l)) {
				return true
			}
			this.mouseDelayMet = !this.options.delay;
			if (!this.mouseDelayMet) {
				this._mouseDelayTimer = setTimeout(function() {
					k.mouseDelayMet = true
				},
				this.options.delay)
			}
			if (this._mouseDistanceMet(l) && this._mouseDelayMet(l)) {
				this._mouseStarted = (this._mouseStart(l) !== false);
				if (!this._mouseStarted) {
					l.preventDefault();
					return true
				}
			}
			this._mouseMoveDelegate = function(n) {
				return k._mouseMove(n)
			};
			this._mouseUpDelegate = function(n) {
				return k._mouseUp(n)
			};
			c(document).bind("mousemove." + this.widgetName, this._mouseMoveDelegate).bind("mouseup." + this.widgetName, this._mouseUpDelegate); (c.browser.safari || l.preventDefault());
			l.originalEvent.mouseHandled = true;
			return true
		},
		_mouseMove: function(j) {
			if (c.browser.msie && !j.button) {
				return this._mouseUp(j)
			}
			if (this._mouseStarted) {
				this._mouseDrag(j);
				return j.preventDefault()
			}
			if (this._mouseDistanceMet(j) && this._mouseDelayMet(j)) {
				this._mouseStarted = (this._mouseStart(this._mouseDownEvent, j) !== false); (this._mouseStarted ? this._mouseDrag(j) : this._mouseUp(j))
			}
			return ! this._mouseStarted
		},
		_mouseUp: function(j) {
			c(document).unbind("mousemove." + this.widgetName, this._mouseMoveDelegate).unbind("mouseup." + this.widgetName, this._mouseUpDelegate);
			if (this._mouseStarted) {
				this._mouseStarted = false;
				this._preventClickEvent = (j.target == this._mouseDownEvent.target);
				this._mouseStop(j)
			}
			return false
		},
		_mouseDistanceMet: function(j) {
			return (Math.max(Math.abs(this._mouseDownEvent.pageX - j.pageX), Math.abs(this._mouseDownEvent.pageY - j.pageY)) >= this.options.distance)
		},
		_mouseDelayMet: function(j) {
			return this.mouseDelayMet
		},
		_mouseStart: function(j) {},
		_mouseDrag: function(j) {},
		_mouseStop: function(j) {},
		_mouseCapture: function(j) {
			return true
		}
	};
	c.ui.mouse.defaults = {
		cancel: null,
		distance: 1,
		delay: 0
	}
})(jQuery);

function getScript(a, b) {
	jQuery.ajax({
		type: "GET",
		url: a,
		success: b,
		dataType: "script",
		cache: true
	})
}
if (!window.console) {
	window.console = {}
}
if (!console.log) {
	console.log = function() {}
}
var _core = function() {
	var obj = this;
	this._coreLoadFile = function() {
		var temp = new Array();
		var tempMethod = function(url, callback) {
			var flag = 0;
			for (i in temp) {
				if (temp[i] == url) {
					flag = 1
				}
			}
			if (flag == 0) {
				temp[temp.length] = url;
				getScript(url,
				function() {
					if ("undefined" != typeof(callback)) {
						if ("function" == typeof(callback)) {
							callback()
						} else {
							eval(callback)
						}
					}
				})
			} else {
				if ("undefined" != typeof(callback)) {
					if ("function" == typeof(callback)) {
						setTimeout(callback, 500)
					} else {
						setTimeout(function() {
							eval(callback)
						},
						500)
					}
				}
			}
		};
		return tempMethod
	};
	this._coreLoadCss = function() {
		var temp = [];
		return function(url) {
			var head = document.getElementsByTagName("head")[0],
			flag = 0,
			link,
			i = temp.length - 1;
			for (; i >= 0; i--) {
				flag = (temp[i] == url) ? 1 : 0
			}
			if (flag == 0) {
				link = document.createElement("link");
				link.setAttribute("rel", "stylesheet");
				link.setAttribute("type", "text/css");
				link.setAttribute("href", url);
				head.appendChild(link);
				temp.push(url)
			}
		}
	};
	this._createImageHtml = function() {
		var _imgHtml = "";
		var _c = function() {
			if (_imgHtml == "") {
				$.post(U("widget/Feed/getSmile", "", 0), {},
				function(data) {
					for (var k in data) {
						_imgHtml += '<a href="javascript:void(0)" title="' + data[k].title + '" onclick="core.face.face_chose(this)" ><img src="' + THEME_URL + data[k].filename + '" width="22" height="22" /></a>'
					}
					_imgHtml += '<div class="c"></div>';
					$("#emot_content").html(_imgHtml)
				},
				"json")
			} else {
				$("#emot_content").html(_imgHtml)
			}
		};
		return _c
	};
	this._rcalendar = function(text, mode, refunc) {
		var temp = 0;
		var tempMethod = function(t, m, r) {
			if (temp == 0) {
				getScript(THEME_URL + "/js/rcalendar.js?v=" + VERSION,
				function() {
					rcalendar(t, m, r)
				})
			} else {
				rcalendar(t, m, r)
			}
			temp++
		};
		return tempMethod
	}
};
var core = new _core();
core.loadFile = core._coreLoadFile();
core.loadCss = core._coreLoadCss();
core.createImageHtml = core._createImageHtml();
core.rcalendar = core._rcalendar();
core.plugInit = function() {
	if (arguments.length > 0) {
		var arg = arguments;
		var back = function() {
			eval("var func = core." + arg[0] + ";");
			if ("undefined" != typeof(func)) {
				func._init(arg)
			}
		};
		var file = THEME_URL + "/js/plugins/core." + arguments[0] + ".js?v=" + VERSION;
		core.loadFile(file, back)
	}
};
core.plugFunc = function(b, c) {
	var a = THEME_URL + "/js/plugins/core." + b + ".js?v=" + VERSION;
	core.loadFile(a, c)
};
core.getChecked = function(b) {
	var a = new Array();
	$.each($("#" + b + " input:checked"),
	function(c, d) {
		if ($(d).val() != "0" && $(d).val() != "") {
			a.push($(d).val())
		}
	});
	return a
};
core.checkOn = function(a) {
	if (a.checked == true) {
		$(a).parents("tr").addClass("bg_on")
	} else {
		$(a).parents("tr").removeClass("bg_on")
	}
};
core.checkAll = function(a, b) {
	if (a.checked == true) {
		$("#" + b + ' input[name="checkbox"]').attr("checked", "true");
		$('tr[overstyle="on"]').addClass("bg_on")
	} else {
		$("#" + b + ' input[name="checkbox"]').removeAttr("checked");
		$('tr[overstyle="on"]').removeClass("bg_on")
	}
};
core.cateMove = function(f, c) {
	var a, b;
	if (c < 0) {
		var e = $("#cate" + f).prev();
		if (e.length > 0) {
			if (!e.hasClass("underline")) {
				e = e.prev()
			}
			a = e.attr("cateid");
			b = e
		} else {
			ui.error(L("PUBLIC_CAN_NOT_MOVE_UP"));
			return false
		}
	} else {
		var d = $("#cate" + f).next();
		if (d.length > 0) {
			if (!d.hasClass("underline")) {
				d = d.next()
			}
			a = d.attr("cateid");
			b = $("#sub_" + a).length > 0 ? $("#sub_" + a) : d
		} else {
			ui.error(L("PUBLIC_CAN_NOT_MOVE_DOWN"));
			return false
		}
	}
	if (c < 0) {
		$("#cate" + f).insertBefore(b)
	} else {
		$("#cate" + f).insertAfter(b)
	}
	if ($("#sub_" + f).length > 0) {
		$("#sub_" + f).insertAfter("#cate" + f)
	}
	return a
};
core.SAjax = function(url, param, callback) {
	$.post(url, param,
	function(msg) {
		if (msg.status == 1) {
			ui.success(msg.info);
			if ("undefined" != typeof(callback)) {
				if ("function" == typeof(callback)) {
					callback(msg)
				} else {
					eval(callback)
				}
			} else {
				setTimeout(function() {
					location.href = location.href
				},
				"1500")
			}
		} else {
			ui.error(msg.info)
		}
	},
	"json")
};
core.loadContent = function(b) {
	var a = "<div class='loading' id='loadMore' >" + L("PUBLIC_LOADING") + "<img src='" + THEME_URL + "/image/load.gif' class='load'></div>";
	b.html(a)
};
core.checkNums = function(e, c, a, d) {
	var f = core.getLeftNums(e, a, true);
	if ("undefined" == typeof(d)) {
		var d = 0
	}
	if (f == a && d == 1) {
		return true
	}
	if (f >= 0) {
		var b = f == a ? "<span>" + f + "</span>": "<span>" + f + "</span>";
		c.innerHTML = b;
		$(e).removeClass("fb");
		if (f == a && $(e).find("img").size() == 0) {
			return false
		}
		return true
	} else {
		var b = L("PUBLIC_INPUT_ERROR_TIPES", {
			sum: '<span style="color:red">' + Math.abs(f) + "</span>"
		});
		$(e).addClass("fb");
		c.innerHTML = b;
		return false
	}
};
core.getLeftNums = function(d, a, c) {
	if ("undefined" != typeof(c)) {
		var g = d.value;
		var f = 0
	} else {
		var e = d.innerHTML;
		var f = $(d).find("img").size();
		var g = e.replace(new RegExp("<br>", "gm"), "");
		g = g.replace(/[ ]|(&nbsp;)*/g, "");
		g = e.replace(/<[^>]*>/g, "");
		g = trim(g, " ");
		if (f < 1) {
			var b = g.replace(/&nbsp;/g, "").replace(/\s+/g, "");
			if (getLength(b) < 1) {
				return a
			} else {
				return a - getLength(b)
			}
		}
		g = g.replace(/&nbsp;/g, "a")
	}
	return a - getLength(g) - f
};
core.validateEmail = function(a) {
	var b = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	return b.test(a)
};
core.stringDb = function(c, b, a) {
	this.inputname = b;
	this.obj = c;
	if (a != "") {
		this.tags = a.split(",")
	} else {
		this.tags = new Array()
	}
	this.taglist = $(c).find("ul");
	this.inputhide = $(c).find("input[name='" + b + "']")
};
core.stringDb.prototype = {
	init: function() {
		if (this.tags.length > 0) {
			var b = "";
			for (var a in this.tags) {
				if (this.tags[a] != "") {
					b += '<li><a class="tag" href="javascript:;">' + this.tags[a] + '</a><a title="' + L("PUBLIC_DELETE") + '" class="ico-close" href="javascript:;"></a></li>'
				}
			}
			this.taglist.html(b);
			this.bindUl()
		}
	},
	bindUl: function() {
		var a = this;
		this.taglist.find(".ico-close").unbind("click");
		this.taglist.find(".ico-close").bind("click",
		function() {
			a.remove($(this).parent().find(".tag").html())
		})
	},
	add: function(a) {
		a = a.replace("，", ",");
		var b = a.split(",");
		var e = this;
		var d = function(h) {
			for (var g in e.tags) {
				if (e.tags[g] == h) {
					return false
				}
			}
			if (getLength(h) > 0) {
				var f = '<li><a class="tag" href="javascript:;">' + h + '</a><a title="' + L("PUBLIC_DELETE") + '" class="ico-close" href="javascript:;"></a></li>';
				e.tags[e.tags.length] = h;
				e.taglist.append(f)
			}
		};
		for (var c in b) {
			if (b[c] != "") {
				d(b[c])
			}
		}
		this.inputhide.val(this.tags.join(","));
		this.bindUl()
	},
	remove: function(b) {
		var c = function(e, f) {
			if (f < 0) {
				return e
			} else {
				return e.slice(0, f).concat(e.slice(parseInt(f) + 1, e.length))
			}
		};
		var a = 0;
		for (var d in this.tags) {
			if (this.tags[d] == b) {
				this.tags = c(this.tags, d);
				this.taglist.find("li").eq(d).remove();
				this.inputhide.val(this.tags.join(","));
				a++
			}
		}
	}
};
core.toggleSubmitCSS = function(b, a) {
	if ("undefined" == typeof(a)) {
		a = 2000
	}
	$(b).toggleClass("btn-ing");
	setTimeout(function() {
		$(b).toggleClass("btn-ing")
	},
	a)
};
core.dnotify = {};
core.dnotify.RequestPermission = function(a) {
	if (!window.webkitNotifications || (window.webkitNotifications.checkPermission() == 0)) {
		return
	}
	window.webkitNotifications.requestPermission(a)
};
core.dnotify.send = function(f, c, b, a, e) {
	if ("undefined" == typeof(f) || "undefined" == typeof(c) || "undefined" == typeof(window.webkitNotifications)) {
		return false
	}
	if ("undefined" == typeof(b) || "" == b) {
		b = U("core/Index/index")
	}
	if ("undefined" == typeof(a) || "" == a) {
		a = SIATIC_URL + "/image/favicon.ico"
	}
	if (window.webkitNotifications.checkPermission() > 0) {
		$("html").bind("click",
		function() {
			core.dnotify.RequestPermission(core.dnotify.send(f, c, b, a, e))
		});
		return
	}
	var d = window.webkitNotifications.createNotification(a, f, c, e);
	d.onclick = function() {
		window.focus();
		window.open(b);
		this.cancel()
	};
	d.show()
};
var CoreSearchUser = function() {};
CoreSearchUser.prototype = {
	init: function(c, b, a, f, e) {
		this.notuser = "";
		this.olduser = "";
		this.searchTime = 0;
		this.input = c;
		this.inputname = this.input.attr("inputname");
		this.userserintval = "";
		this.userList = "";
		this.follow = "undefined" == typeof(b) ? 1 : b;
		this.max = "undefined" == typeof(a) ? 0 : a;
		this.noself = "undefined" == typeof(e) ? 1 : e;
		this.stoploop = 0;
		if ("undefined" != typeof(f)) {
			this.callback = f
		} else {
			this.callback = ""
		}
		var d = this;
		this.input.blur(function() {
			var g = function() {
				d._stopUser();
				d._removeUserList()
			};
			setTimeout(g, 150)
		});
		this._removeUserList();
		this._startUser()
	},
	insertUser: function(f, k, g) {
		if (f == "0") {
			return false
		}
		if ("undefined" == typeof(this.input.prev()) || $("#search_uids_" + this.inputname).length < 1) {
			$('<input type="hidden" value="' + f + '" name="' + this.inputname + '" id="search_uids_' + this.inputname + '"+ rel="uids">').insertBefore(this.input)
		}
		var h = $("#search_uids_" + this.inputname);
		if (h.prev().attr("rel") != "userlist") {
			$('<ul class="user-list" rel = "userlist" ></ul>').insertBefore(h)
		}
		var j = h.prev();
		var d = h.val();
		var a = d.split(",");
		for (var b in a) {
			if (a[b] == f) {
				ui.error(L("PUBLIC_USER_ISNOT_TIPES"));
				return false
			}
		}
		var l = 1;
		if (this.max > 0) {
			if (d != "" && d != 0) {
				var a = d.split(",");
				if (a.length >= this.max) {
					ui.error(L("PUBLIC_SELECT_USER_TIPES", {
						user: this.max
					}));
					return false
				}
				if (a.length + 1 >= this.max) {
					this.input.blur();
					this.input.hide();
					l = 0
				}
			} else {
				if (this.max == 1) {
					this.input.blur();
					this.input.hide();
					l = 0
				}
			}
		}
		var c = '<li><div class="content"><a href="javascript:;">' + k + "</a><span>(" + g + ')</span></div><a class="ico-close right" href="javascript:;" uid=' + f + " inputname=" + this.inputname + "></a></li>";
		j.append(c);
		if (d != "" && d != "0") {
			h.val(d + "," + f)
		} else {
			h.val(f)
		}
		var e = this;
		j.find(".ico-close").click(function() {
			e.removeUser($(this).attr("uid"), this, $(this).attr("inputname"))
		});
		this.input.val("");
		this.inputReset(1);
		return true
	},
	inputReset: function(a) {
		this.olduser = "";
		this._stopUser();
		this._removeUserList();
		this.stoploop = 0;
		if (a == 1) {
			this.init(this.input, this.follow, this.max, this.callback, this.noself)
		}
	},
	selectUser: function(e) {
		if (typeof(e.userList) == "string") {
			return false
		}
		var b = e.userList.find(".mod-at-list").find(".current");
		if (b.length > 0) {
			var d = b.attr("uid");
			var a = b.attr("uname");
			var c = b.attr("email");
			e.insertUser(d, a, c)
		} else {
			return true
		}
		return true
	},
	removeUser: function(e, g, b) {
		var d = $("#search_uids_" + b);
		$(g).parent().remove();
		d.next().show();
		var f = d.val();
		var a = f.split(",");
		var h = new Array();
		for (var c in a) {
			if (a[c] != e && a[c] != "" && "string" == typeof(a[c])) {
				h[h.length] = a[c]
			}
		}
		d.val(h.join(","));
		this._removeUserList()
	},
	_startUser: function() {
		var b = this;
		var a = function() {
			if (b.stoploop == 1) {
				return true
			}
			var c = function(f, e) {
				$.post(U("widget/SearchUser/search"), {
					key: e,
					follow: b.follow,
					noself: b.noself
				},
				function(l) {
					if (f != b.searchTime) {
						return false
					}
					if (l.status == 0 || l.data == null || l.data == "" || l.data.length == 0) {
						b.notuser = e;
						b.userList.find(".mod-at-list").html("<div class='mod-at-tips'>" + L("PUBLIC_SEARCH_USER_TIPES") + "</div>");
						return false
					} else {
						if ("function" == typeof(b.callback)) {
							b.callback(l.data);
							return false
						}
						var j = l.data;
						b.notuser = "";
						if (j.length > 0) {
							var h = '<ul class="at-user-list">';
							for (var g in j) {
								var k = g == 0 ? " class='current'": "";
								h += '<li uid ="' + j[g].id + '" uname="' + j[g].name + '"    email="' + j[g].note + '" ' + k + '><div class="face"><img src="' + j[g].img + '" width="20px" height="20px" /></div><div class="content"><a href="javascript:void(0)">' + j[g].name + "</a><span>" + j[g].note + "</span></div></li>"
							}
							h += "</ul>";
							b.userList.find(".mod-at-list").html(h);
							b.userList.find(".mod-at-list").find("li").hover(function() {
								$(this).addClass("hover")
							},
							function() {
								$(this).removeClass("hover")
							});
							b.userList.find(".mod-at-list").find("li").click(function() {
								var p = $(this).attr("uid");
								var n = $(this).attr("uname");
								var o = $(this).attr("email");
								b.insertUser(p, n, o)
							});
							var m = function() {
								b.selectUser(b)
							};
							core.plugInit("bindkey", $(b.userList.find(".mod-at-list")), "li", "current", m)
						} else {
							b.insertUser(j.uid, j.uname, j.email);
							b._removeUserList()
						}
					}
				},
				"json")
			};
			var d = b.input.val();
			if (d == "" || d == null) {
				b.olduser = "";
				b._createUserlistDiv();
				d = b.olduser = "-1"
			} else {
				if ((b.notuser != "" && d.indexOf(b.notuser) >= 0) || b.olduser == d) {
					return false
				} else {
					b.olduser = d;
					b.searchTime++;
					b._createUserlistDiv();
					c(b.searchTime, d)
				}
			}
		};
		this.userserintval = setInterval(a, 250)
	},
	_stopUser: function() {
		this.stoploop = 1;
		if (this.userserintval != "") {
			clearInterval(this.userserintval);
			this.userserintval = ""
		}
		this._removeUserList()
	},
	_createUserlistDiv: function() {
		if (typeof(this.userList) != "string") {
			return false
		}
		var a = "<div class='mod-at-wrap' id='message_box'><div class='mod-at'><div class='mod-at-list'><div class='mod-at-tips'>" + L("PUBLIC_PLEASE_SEARCH_USER") + "</div></div></div></div>";
		this.userList = $(a);
		this.userList.appendTo("body");
		var b = this;
		this._showUserList()
	},
	_showUserList: function() {
		var a = this.input.offset();
		if (this.input[0].style.display == "none") {
			this.userList.css({
				left: a.left + "px",
				top: (a.top + this.input.height() + 14) + "px",
				display: "none"
			})
		} else {
			this.userList.css({
				left: a.left + "px",
				top: (a.top + this.input.height() + 14) + "px",
				display: "block"
			})
		}
		if (this.input.attr("user_list_width") != 0) {
			this.userList.css("width", this.input.attr("user_list_width") + "px")
		}
	},
	_removeUserList: function() {
		if ($("#message_box").length > 0) {
			$("#message_box").remove()
		}
		if (this.userList.length > 0 && "string" != typeof(this.userList)) {
			this.userList.remove()
		}
		this.userList = ""
	}
};
core.facecard = {
	_init: function(a) {
		this.init()
	},
	init: function() {
		if ("undefined" != typeof(this.face_box)) {
			return false
		}
		var a = '<div id="face_card" class="M-layer" style="position:absolute;left:10%;background-color:#fff;display:none;z-index:8000"><div class="M-content"><div class="bd"><div class="name-card clearfix"><div class="loading"><img src="' + THEME_URL + '/image/load.gif" class="load"></div></div></div><div class="arrow ico-arrow-left" ></div></div></div>';
		this.face_box = $(a);
		$("body").append(this.face_box);
		this.user_info = new Array()
	},
	show: function(b, a) {
		this.obj = b;
		$(b).attr("show", "yes");
		var d = this;
		var c = function() {
			if ($(b).attr("show") != "yes") {
				return false
			}
			if ("undefined" != typeof(d.user_info[a]) || d.user_info[a] == "") {
				d.face_box.find(".name-card").html(d.user_info[a]);
				d.setCss(b)
			} else {
				$.post(U("core/Index/showFaceCard"), {
					uid: a
				},
				function(e) {
					d.face_box.find(".name-card").html(e);
					d.setCss(b);
					d.user_info[a] = e
				})
			}
		};
		setTimeout(c, 250);
		$(b).mouseover(function() {
			$(this).attr("show", "yes")
		});
		$(b).mouseout(function() {
			$(this).attr("show", "no")
		})
	},
	deleteUser: function(a) {
		if ("undefined" != typeof(this.user_info) && this.user_info.uid != "") {
			this.user_info[a] = "";
			delete this.user_info[a]
		}
	},
	setCss: function(e) {
		var a = $(e).offset();
		var d = $("body").height();
		var f = $(window).width();
		var l = $(window).scrollTop();
		var c = this.face_box.width();
		var m = this.face_box.height();
		var j = a.top - m - 5;
		var h = "arrow-b";
		var b = a.left - 18;
		if (f - a.left < c) {
			b = a.left - c - 5;
			h = "ico-arrow-right";
			j = a.top - 5
		}
		if (a.top - l < 40 + m) {
			j = a.top + $(e).height() + 5;
			b = a.left - 15;
			h = "arrow-t"
		}
		if (d - a.top < m || ($(window).height() + l - a.top) < m) {
			j = a.top - m - 5;
			h = "arrow-b";
			b = a.left - 18
		}
		var k = this.face_box.find(".arrow");
		k.removeClass("ico-arrow-right");
		k.removeClass("ico-arrow-left");
		k.removeClass("arrow-b");
		k.removeClass("arrow-t");
		k.addClass(h);
		this.face_box.css({
			left: b + "px",
			top: j + "px"
		});
		this.face_box.show();
		var g = this;
		this.face_box.mouseover(function() {
			g.boxOn = true
		});
		this.face_box.mouseout(function() {
			g.boxOn = false;
			g.hide()
		})
	},
	hide: function() {
		var b = this;
		var a = function() {
			if (b.boxOn || $(b.obj).attr("show") == "yes") {
				return false
			}
			b.face_box.hide();
			$(b.obj).attr("show", "no")
		};
		setTimeout(a, 250)
	},
	dohide: function() {
		var a = this;
		if ("undefined" != typeof(a.face_box)) {
			a.face_box.hide();
			$(a.obj).attr("show", "no")
		}
	}
};
core.comment = {
	_init: function(a) {
		if (a.length == 3) {
			core.comment.init(a[1], a[2])
		} else {
			return false
		}
	},
	init: function(a, b) {
		this.row_uid = a.row_uid,
		this.row_id = a.row_id,
		this.to_comment_id = a.to_comment_id,
		this.to_uid = a.to_uid;
		this.app_row_id = a.app_row_id;
		this.addToEnd = "undefined" == typeof(a.addToEnd) ? 0 : a.addToEnd;
		this.canrepost = "undefined" == typeof(a.canrepost) ? 1 : a.canrepost;
		this.cancomment = "undefined" == typeof(a.cancomment) ? 1 : a.cancomment;
		this.cancomment_old = "undefined" == typeof(a.cancomment_old) ? 1 : a.cancomment_old;
		this.canshare = "undefined" == typeof(a.canshare) ? 1 : a.canshare;
		if ("undefined" != typeof(a.app_name)) {
			this.app_name = a.app_name
		} else {
			this.app_name = "core"
		}
		if ("undefined" != typeof(a.model_name)) {
			this.model_name = a.model_name
		} else {
			this.model_name = "feed"
		}
		if ("undefined" != typeof(a.to_comment_uname)) {
			this.to_comment_uname = a.to_comment_uname
		}
		if ("undefined" != typeof(b)) {
			this.commentListObj = b
		}
	},
	display: function() {
		var a = this.commentListObj;
		if ("undefined" == typeof this.model_name) {
			this.model_name = "feed"
		}
		if (a.style.display == "none") {
			if (a.innerHTML != "") {
				$(a).parent().show();
				$(a).show();
				$(a).find(".input-comment").focus()
			} else {
				$.post(U("widget/Comment/render"), {
					row_uid: this.row_uid,
					row_id: this.row_id,
					app_row_id: this.app_row_id,
					isAjax: 1,
					cancomment: this.cancomment,
					cancomment_old: this.cancomment_old,
					app_name: this.app_name,
					model_name: this.model_name,
					canrepost: this.canrepost,
					canshare: this.canshare
				},
				function(b) {
					if (b.status == "0") {
						ui.error(b.data)
					} else {
						$(a).show();
						a.innerHTML = b.data;
						$(a).parent().show();
						$(a).find(".input-comment").focus();
						M(a);
						var d = $(a.parentModel).find(".arrow").offset().left;
						var c = $(a.parentModel.childEvents.comment[0]).offset().left - d;
						$(a.parentModel).find(".arrow").css("left", c)
					}
				},
				"json")
			}
		} else {
			$(a).parent().hide();
			$(a).hide()
		}
	},
	initReply: function() {
		this.comment_textarea = this.commentListObj.childModels.comment_textarea[0];
		var a = this.comment_textarea.childEvents.mini_editor_textarea[0];
		var b = L("PUBLIC_RESAVE") + "@" + this.to_comment_uname + ": ";
		a.focus();
		a.value = "";
		a.value = b
	},
	addComment: function(c) {
		var f = this.commentListObj;
		this.comment_textarea = f.childModels.comment_textarea[0];
		var b = $(this.commentListObj).find(".input-comment")[0];
		if (core.getLeftNums(b, $config.initNums, true) < 0) {
			flashTextarea(b);
			ui.error(L("PUBLIC_COMMENT_MAX_LENGTH", {
				num: $config.initNums
			}));
			return false
		}
		var d = b.value;
		if (d == "") {
			ui.error(L("PUBLIC_CONCENT_TIPES"));
			return false
		}
		if ($(this.comment_textarea).find("input[name='shareFeed']").attr("checked") == "checked") {
			var h = 1
		} else {
			var h = 0
		}
		if ($(this.comment_textarea).find("input[name=comment]").attr("checked") == "checked") {
			var a = 1
		} else {
			var a = 0
		}
		if ("undefined" != typeof(this.addCommentIng) && (this.addCommentIng == true)) {
			return false
		}
		var e = this.addToEnd;
		var g = this;
		g.addCommentIng = true;
		$.post(U("widget/Comment/addcomment"), {
			app_name: this.app_name,
			model_name: this.model_name,
			row_uid: this.row_uid,
			row_id: this.row_id,
			to_comment_id: this.to_comment_id,
			to_uid: this.to_uid,
			app_row_id: this.app_row_id,
			content: d,
			ifShareFeed: h,
			comment_old: a
		},
		function(j) {
			g.addCommentIng = false;
			if (j.status == "0") {
				ui.error(j.data)
			} else {
				if ("undefined" != typeof(f.childModels.comment_list)) {
					if (e == 1) {
						$(f).find(".comment_lists").eq(0).append(j.data)
					} else {
						if ($(f).find(".comment_list").size() > 0) {
							$(j.data).insertBefore($(f).find(".comment_list")[0])
						} else {
							$(f).find(".comment_lists").html(j.data)
						}
					}
				} else {
					$(f).find(".comment_lists").eq(0).html(j.data)
				}
				$(f).find(".comment_lists").eq(0).show();
				M(f);
				g.resetCommentNums(g.row_id, 1);
				b.value = "";
				g.to_comment_id = 0;
				g.to_uid = 0;
				if ("function" == typeof(c)) {
					c()
				}
			}
		},
		"json")
	},
	resetCommentNums: function(b, a) {
		$(".comment_nums").each(function() {
			if ($(this).attr("row_id") == b) {
				var c = parseInt($(this).html());
				if (a > 0) {
					$(this).html(c + 1)
				} else {
					if (c > 0) {
						$(this).html(c - 1)
					}
				}
			}
		})
	},
	delComment: function(b, a) {
		var c = this;
		$.post(U("widget/Comment/delcomment"), {
			comment_id: b
		},
		function(d) {
			c.resetCommentNums(a, -1)
		})
	},
	loadMore: function(c, a, b) {
		$.post(U("widget/Comment/loadMore"), a,
		function(d) {
			$(c).parent().remove();
			$(b).append(d);
			M(b)
		})
	}
};
var CoreSearch = function(b, a) {
	this.obj = b;
	this.args = a;
	this.resultList = null;
	this.oldKey = "";
	this.noDataKey = null;
	this.stoploop = 0;
	this.searchTime = 0;
	this.searchIntval = null;
	this.search_tips = ("undefined" == typeof a.search_tips && a.search_tips != "") ? a.search_tips: L("PUBLIC_PLEASE_SEARCH_USER")
};
CoreSearch.prototype = {
	init: function() {
		var b = this;
		if (this.args.default_ids != "") {
			$(this.obj.childModels.search_list[0]).show()
		}
		if ("undefined" != typeof(this.obj.childEvents.search_link)) {
			$(this.obj.childEvents.search_link[0]).blur(function() {
				setTimeout(function() {
					b.stopFind()
				},
				250)
			});
			$(this.obj.childEvents.search_link[0]).focus(function() {
				this.value = "";
				b.startFind()
			});
			var a = $(this.obj.childEvents.search_link[0]);
			a.bind("keydown",
			function(c) {
				if (a.val() != "") {
					return true
				}
				var d = $(this.parentModel).find("ul li:last");
				if (c.keyCode == 8 || c.keyCode == 46) {
					if (d.length > 0) {
						d.find(".ico-close").click()
					}
				}
			})
		}
		$(this.obj.childModels.search_list[0]).find(".ico-close").click(function() {
			b.removeOne($(this).attr("search_id"), this, b.args.inputname)
		});
		$("#search_ids_" + this.args.inputname).val(this.args.default_ids);
		if ("undefined" != typeof(b.args.search_tips) && b.args.search_tips != "") {
			this.search_tips = b.args.search_tips
		}
	},
	startFind: function() {
		var b = this;
		var a = function() {
			if (b.stoploop == 1) {
				return true
			}
			var c = function(f, e) {
				$.post(U("widget/Search/search"), {
					key: e,
					model_name: b.args.model_name,
					app_name: b.args.app_name,
					search_method: b.args.search_method
				},
				function(n) {
					if (f != b.searchTime || b.stoploop == 1) {
						return false
					}
					if (n.status == 0 || n.data == null || n.data == "" || n.data.length == 0) {
						b.noDataKey = e;
						b._createListDiv(1);
						return false
					} else {
						var l = n.data;
						b.noDataKey = "";
						var k = '<ul class="at-user-list">';
						for (var j in l) {
							var m = j == 0 ? " class='current'": "";
							var h = (b.args.mulit == 1 && l[j].mulit != "") ? l[j].mulit: l[j].name;
							k += '<li search_id="' + l[j].id + '" search_name="' + h + '"  search_note="' + l[j].note + '" ' + m + ">";
							if (l[j].img != "") {
								k += '<div class="face"><img src="' + l[j].img + '" width="20px" height="20px" /></div>'
							}
							k += '<div class="content"><a href="javascript:void(0)">' + l[j].name + "</a><span>" + l[j].note + "</span></div></li>"
						}
						k += "</ul>";
						b.resultList.find(".mod-at-list").html(k);
						b.resultList.find(".mod-at-list").find("li").hover(function() {
							$(this).addClass("hover")
						},
						function() {
							$(this).removeClass("hover")
						});
						b.resultList.find(".mod-at-list").find("li").click(function() {
							var o = $(this).attr("search_id");
							var p = $(this).attr("search_name");
							var q = $(this).attr("search_note");
							b.insertOne(o, p, q)
						});
						var g = function() {
							b.selectList(b);
							if (typeof(b.resultList) == "object" && b.resultList != null) {
								b.resultList.remove()
							}
							b.resultList = null
						};
						core.plugInit("bindkey", b.resultList.find(".mod-at-list"), "li", "current", g)
					}
				},
				"json")
			};
			var d = b.obj.childEvents.search_link[0].value;
			if (d == "" || d == null) {
				b.oldKey = "";
				b._createListDiv(0);
				d = b.oldKey = "-1"
			} else {
				if ((b.noDataKey != null && getLength(b.noDataKey) > 0 && d.indexOf(b.noDataKey) >= 0) || b.oldKey == d) {
					return false
				} else {
					b.oldKey = d;
					b.searchTime++;
					b._createListDiv(0);
					c(b.searchTime, d)
				}
			}
		};
		b.stoploop = 0;
		b.searchIntval = setInterval(a, 251)
	},
	insertOne: function(search_id, search_name, search_note) {
		if (search_id == "0") {
			return false
		}
		var _this = this;
		var search_idsInput = $("#search_ids_" + this.args.inputname);
		var dllist = $(this.obj.childModels.search_list[0]);
		var search_ids = search_idsInput.val();
		var _search_ids = search_ids.split(",");
		var _search_name = search_name.split(",");
		var html = "";
		var _getHtml = function(search_name, search_note, search_id) {
			var html = '<li><div class="content"><a href="javascript:;" title="' + search_name + '">' + search_name + "</a>";
			if (search_note != "") {
				html += "<span>(" + search_note + ")</span>"
			}
			html += '</div><a class="ico-close right" href="javascript:;" search_id=' + search_id + "></a></li>";
			return html
		};
		if (_search_name.length > 1) {
			var _search_id = search_id.split(",");
			var _search_name_res = new Array();
			var _search_id_res = new Array();
			for (var i in _search_name) {
				var fonund = 0;
				for (var _i in _search_ids) {
					if (_search_ids[_i] == _search_id[i]) {
						fonund = 1
					}
				}
				if (fonund == 0) {
					html += _getHtml(_search_name[i], _search_name[i], _search_id[i]);
					_search_name_res[i] = _search_name[i];
					_search_id_res[i] = _search_id[i]
				}
			}
			search_id = _search_id_res.join(",");
			_search_name = _search_name_res;
			var mulit = _search_name.length;
			if (mulit == 0) {
				ui.error(L("PUBLIC_USER_ISNOT_TIPES"));
				return false
			}
		} else {
			var mulit = 0;
			for (var _i in _search_ids) {
				if (_search_ids[_i] == search_id) {
					ui.error(L("PUBLIC_USER_ISNOT_TIPES"));
					return false
				}
			}
		}
		if (this.args.max > 0) {
			if (mulit != 0 && (mulit + _search_ids.length) > this.args.max) {
				ui.error(L("PUBLIC_SELECT_USER_TIPES", {
					user: this.args.max
				}));
				return false
			}
			if (search_ids != "" && search_ids != 0) {
				if (_search_ids.length >= this.args.max) {
					ui.error(L("PUBLIC_SELECT_USER_TIPES", {
						user: this.args.max
					}));
					return false
				}
				if (_search_ids.length + 1 >= this.max) {
					_this.stopFind();
					$(this.obj.childEvents.search_link[0]).hide()
				}
			} else {
				if (this.args.max == 1) {
					_this.stopFind();
					$(this.obj.childEvents.search_link[0]).hide()
				}
			}
		}
		if ("undefined" != typeof(_this.args.callbackHtml)) {
			eval("var html = " + _this.args.callbackHtml + "(search_name,search_id,search_note)")
		} else {
			if (html == "") {
				html = _getHtml(search_name, search_note, search_id)
			}
		}
		dllist.append(html);
		dllist.show();
		if (search_ids != "" && search_ids != "0") {
			search_idsInput.val(search_ids + "," + search_id)
		} else {
			search_idsInput.val(search_id)
		}
		dllist.find(".ico-close").click(function() {
			_this.removeOne($(this).attr("search_id"), this, _this.args.inputname)
		});
		$(this.obj.childEvents.search_link[0]).val("");
		return true
	},
	removeAll: function() {
		$("#search_ids_" + this.args.inputname).val("");
		$(this.obj.childModels.search_list[0]).html("")
	},
	removeOne: function(b, g, c) {
		var e = $("#search_ids_" + c);
		$(g).parent().remove();
		$(this.obj.childEvents.search_link[0]).show();
		var f = e.val();
		var a = f.split(",");
		var h = new Array();
		for (var d in a) {
			if (a[d] != b && a[d] != "" && "string" == typeof(a[d])) {
				h[h.length] = a[d]
			}
		}
		e.val(h.join(","))
	},
	selectList: function(d) {
		if (typeof(d.resultList) == "string") {
			return false
		}
		var e = d.resultList.find(".mod-at-list").find(".current");
		if (e.length > 0) {
			var a = e.attr("search_id");
			var b = e.attr("search_name");
			var c = e.attr("search_note");
			d.insertOne(a, b, c)
		} else {
			return true
		}
		return true
	},
	_createListDiv: function(b) {
		if (this.resultList != null && typeof(this.resultList) != "string" && b != 1) {
			$(this.resultList).show();
			return false
		}
		$("#search_" + this.args.inputname).remove();
		var c = "<div id='search_" + this.args.inputname + "' class='mod-at-wrap' ><div class='mod-at'><div class='mod-at-list'><div class='mod-at-tips'>" + this.search_tips + "</div></div></div></div>";
		this.resultList = $(c);
		this.resultList.appendTo($("body"));
		var a = $(this.obj.childEvents.search_link[0]).offset();
		if (this.obj.childEvents.search_link[0].style.display == "none") {
			this.resultList.css({
				left: a.left + "px",
				top: (a.top + $(this.obj.childEvents.search_link[0]).height() + 14) + "px",
				display: "none"
			})
		} else {
			this.resultList.css({
				left: a.left + "px",
				top: (a.top + $(this.obj.childEvents.search_link[0]).height() + 14) + "px",
				display: "block"
			})
		}
		if (this.args.list_width != 0) {
			this.resultList.css("width", this.args.list_width + "px")
		}
	},
	stopFind: function() {
		clearInterval(this.searchIntval);
		this.stoploop = 1;
		if (typeof(this.resultList) == "object" && this.resultList != null) {
			this.resultList.remove();
			this.resultList = null
		}
	}
};
var U = function(c, f, e) {
	if ("undefined" == typeof(e)) {
		var e = 1
	}
	c = c.split("/");
	if (c[0] == "" || c[0] == "@") {
		c[0] = $config.app_name
	}
	if (!c[1]) {
		c[1] = "Index"
	}
	if (!c[2]) {
		c[2] = "index"
	}
	var b = $config.url_mod == 1 ? $config.host_url + "/index.php?app=" + c[0] + "&mod=" + c[1] + "&act=" + c[2] : $config.host_url + "/" + c[0] + "/" + c[1] + "/" + c[2];
	if (f) {
		var a = "";
		for (var d in f) {
			if (/[^\d]/.test(d)) {
				a += (a == "" ? d + "=" + f[d] : "&" + d + "=" + f[d])
			} else {
				a += (a == "" ? f[d] : "&" + f[d])
			}
		}
		if (e == 1) {
			b += ($config.url_mod == 1 ? "&": "?") + a + "&token=" + $config.token
		} else {
			b += ($config.url_mod == 1 ? "&": "?") + a
		}
	} else {
		if ($config.url_mod == 2) {
			if (e == 1) {
				b += "?token=" + $config.token
			} else {
				b += "?"
			}
		}
	}
	return b
};
var getLength = function(b, a) {
	if (true == a) {
		return Math.ceil(b.replace(/((news|telnet|nttp|file|http|ftp|https):\/\/){1}(([-A-Za-z0-9]+(\.[-A-Za-z0-9]+)*(\.[-A-Za-z]{2,5}))|([0-9]{1,3}(\.[0-9]{1,3}){3}))(:[0-9]*)?(\/[-A-Za-z0-9_\$\.\+\!\*\(\),;:@&=\?\/~\#\%]*)*/ig, "xxxxxxxxxxxxxxxxxxxx").replace(/^\s+|\s+$/ig, "").replace(/[^\x00-\xff]/ig, "xx").length / 2)
	} else {
		return Math.ceil(b.replace(/^\s+|\s+$/ig, "").replace(/[^\x00-\xff]/ig, "xx").length / 2)
	}
};
var subStr = function(f, a, d) {
	var d = "undefined" == typeof(d) ? "": d;
	if (!f) {
		return ""
	}
	a = a > 0 ? a * 2 : 280;
	var e = 0,
	b = "";
	for (var c = 0; c < f.length; c++) {
		if (f.charCodeAt(c) > 255) {
			e += 2
		} else {
			e++
		}
		if (e > a) {
			return b + d
		}
		b += f.charAt(c)
	}
	return f
};
function L(b, d) {
	if ("undefined" == typeof(LANG[b])) {
		return b
	}
	if ("object" != typeof(d)) {
		return LANG[b]
	} else {
		var c = LANG[b];
		for (var a in d) {
			c = c.replace("{" + a + "}", d[a])
		}
		return c
	}
}
var updateUserData = function(f, a, e) {
	var d = M.nodes.events[f];
	if ("undefined" == typeof(e)) {
		var e = $config.uid
	}
	if ("undefined" == typeof(d)) {
		return false
	}
	for (var c in d) {
		var g = d[c];
		var b = M.getEventArgs(g);
		if (b.uid == e) {
			g.innerHTML = parseInt(g.innerHTML, 10) + parseInt(a, 10)
		}
	}
	return false
};
var flashTextarea = function(c) {
	var b = 0;
	var a = function() {
		if (b > 3) {
			return false
		}
		if ($(c).hasClass("fb")) {
			$(c).removeClass("fb")
		} else {
			$(c).addClass("fb")
		}
		setTimeout(a, 310);
		b++
	};
	a();
	return false
}; (function(a) {
	a.fn.extend({
		insertAtCaret: function(d) {
			var f = a(this)[0];
			if (document.selection) {
				this.focus();
				sel = document.selection.createRange();
				sel.text = d;
				this.focus()
			} else {
				if (f.selectionStart || f.selectionStart == "0") {
					var c = f.selectionStart;
					var b = f.selectionEnd;
					var e = f.scrollTop;
					f.value = f.value.substring(0, c) + d + f.value.substring(b, f.value.length);
					this.focus();
					f.selectionStart = c + d.length;
					f.selectionEnd = c + d.length;
					f.scrollTop = e
				} else {
					this.value += d;
					this.focus()
				}
			}
		}
	})
})(jQuery);
var scrolltotop = {
	setting: {
		startline: 100,
		scrollto: 0,
		scrollduration: 500,
		fadeduration: [500, 100]
	},
	controlHTML: '<a href="#top" class="top_stick">&nbsp;</a>',
	controlattrs: {
		offsetx: 20,
		offsety: 20
	},
	anchorkeyword: "#top",
	state: {
		isvisible: false,
		shouldvisible: false
	},
	scrollup: function() {
		if (!this.cssfixedsupport) {
			this.$control.css({
				opacity: 0
			})
		}
		var a = isNaN(this.setting.scrollto) ? this.setting.scrollto: parseInt(this.setting.scrollto);
		if (typeof a == "string" && jQuery("#" + a).length == 1) {
			a = jQuery("#" + a).offset().top
		} else {
			a = this.setting.scrollto
		}
		this.$body.animate({
			scrollTop: a
		},
		this.setting.scrollduration)
	},
	keepfixed: function() {
		var c = jQuery(window);
		var b = c.scrollLeft() + c.width() - this.$control.width() - this.controlattrs.offsetx;
		var a = c.scrollTop() + c.height() - this.$control.height() - this.controlattrs.offsety;
		this.$control.css({
			left: b + "px",
			top: a + "px"
		})
	},
	togglecontrol: function() {
		var a = jQuery(window).scrollTop();
		if (!this.cssfixedsupport) {
			this.keepfixed()
		}
		this.state.shouldvisible = (a >= this.setting.startline) ? true: false;
		if (this.state.shouldvisible && !this.state.isvisible) {
			this.$control.stop().animate({
				opacity: 0.3
			},
			this.setting.fadeduration[0]);
			this.state.isvisible = true
		} else {
			if (this.state.shouldvisible == false && this.state.isvisible) {
				this.$control.stop().animate({
					opacity: 0
				},
				this.setting.fadeduration[1]);
				this.state.isvisible = false
			}
		}
	},
	init: function() {
		jQuery(document).ready(function(c) {
			var a = scrolltotop;
			var b = document.all;
			a.cssfixedsupport = !b || b && document.compatMode == "CSS1Compat" && window.XMLHttpRequest;
			a.$body = (window.opera) ? (document.compatMode == "CSS1Compat" ? c("html") : c("body")) : c("html,body");
			a.$control = c('<div id="topcontrol">' + a.controlHTML + "</div>").css({
				position: a.cssfixedsupport ? "fixed": "absolute",
				bottom: a.controlattrs.offsety,
				right: a.controlattrs.offsetx,
				opacity: 0,
				cursor: "pointer"
			}).attr({
				title: L("PUBLIC_MOVE_TOP")
			}).click(function() {
				a.scrollup();
				return false
			}).appendTo("body");
			if (document.all && !window.XMLHttpRequest && a.$control.text() != "") {
				a.$control.css({
					width: a.$control.width()
				})
			}
			a.togglecontrol();
			c('a[href="' + a.anchorkeyword + '"]').click(function() {
				a.scrollup();
				return false
			});
			c(window).bind("scroll resize",
			function(d) {
				a.togglecontrol()
			})
		})
	}
};
scrolltotop.init();
var ui = {
	sendmessage: function(a) {
		a = a || "";
		this.box.load(U("core/Message/post", "", 0) + "&touid=" + a, L("PUBLIC_SETPRIVATE_MAIL"))
	},
	sendat: function(a) {
		a = a || "";
		this.box.load(U("core/Mention/at", "", 0) + "&touid=" + a, "@TA")
	},
	reply: function(a) {
		this.box.load(U("core/Comment/reply", "", 0) + "&comment_id=" + a, L("PUBLIC_RESAVE"))
	},
	changeDepartment: function(c, a, b, e, d) {
		this.box.load(U("widget/Department/change", "", 0) + "&hid=" + c + "&showName=" + a + "&sid=" + b + "&nosid=" + e + "&notop=" + d, L("PUBLIC_DEPATEMENT_SELECT"))
	},
	showMessage: function(l, g, d) {
		var a = (g == "1") ? "html_clew_box clew_error ": "html_clew_box";
		var f = (g == "1") ? "aicon-exclamation-sign": "aicon-ok-sign";
		var e = '<div class="' + a + '" id="ui_messageBox" style="display:none"><div class="html_clew_box_con" id="ui_messageContent"><i class="' + f + '"></i>' + l + "</div></div>";
		var k = function() {
			for (var m = 0; m < arguments.length; m++) {
				if (typeof arguments[m] != "undefined") {
					return false
				}
			}
			return true
		};
		$(e).appendTo(document.body);
		var c = $("#ui_messageBox").height();
		var j = $("#ui_messageBox").width();
		var b = ($("body").width() - j) / 2;
		var h = $(window).scrollTop() + ($(window).height() - c) / 2;
		$("#ui_messageBox").css({
			left: b + "px",
			top: h + "px"
		}).fadeIn("slow",
		function() {
			$("#ui_messageBox")
		});
		setTimeout(function() {
			$("#ui_messageBox").find("iframe").remove();
			$("#ui_messageBox").fadeOut(function() {
				$("#ui_messageBox").remove()
			})
		},
		d * 1000)
	},
	showblackout: function() {
		if ($(".boxy-modal-blackout").length > 0) {} else {
			var a = $("body").height() > $(document).height() ? $("body").height() : $(document).height();
			$('<div class ="boxy-modal-blackout" ></div>').css({
				height: a + "px",
				width: $("body").width() + "px",
				zIndex: 999
			}).appendTo(document.body)
		}
	},
	removeblackout: function() {
		if ($("#uibox").length > 0) {
			if (document.getElementById("uibox").style.display == "none") {
				$(".boxy-modal-blackout").remove()
			}
		} else {
			$(".boxy-modal-blackout").remove()
		}
	},
	success: function(b, c) {
		var a = "undefined" == typeof(c) ? 1 : c;
		ui.showMessage(b, 0, a)
	},
	error: function(b, c) {
		var a = "undefined" == typeof(c) ? 1.5 : c;
		ui.showMessage(b, 1, a)
	},
	confirm: function(o, text, _callback) {
		if ($("#ui_box_confirm").length > 0) {
			$("#ui_box_confirm").remove()
		}
		var callback = "undefined" == typeof(_callback) ? $(o).attr("callback") : _callback;
		text = text || L("PUBLIC_ACCONT_TIPES");
		text = "<i class='aicon-exclamation-sign'></i>" + text;
		this.html = '<div id="ui_box_confirm" class="ui_confirm"><div class="layer-mini-info"><dl><dt class="txt"> </dt><dd class="action"><a class="btn btn-blue-mini mr10" href="javascript:void(0)"><span>' + L("PUBLIC_QUEDING") + '</span></a><a class="btn btn-mini" href="javascript:void(0)"><span>' + L("PUBLIC_QUXIAO") + "</span></a></dd></dl></div></div>";
		$("body").append(this.html);
		var position = $(o).offset();
		$("#ui_box_confirm").css({
			top: position.top + "px",
			left: (position.left - 120 + $(o).width() / 2) + "px",
			display: "none"
		});
		$("#ui_box_confirm .txt").html(text);
		$("#ui_box_confirm").fadeIn("fast");
		$("#ui_box_confirm .btn-mini").one("click",
		function() {
			$("#ui_box_confirm").fadeOut("fast");
			$("#ui_box_confirm").remove();
			return false
		});
		$("#ui_box_confirm .btn-blue-mini").one("click",
		function() {
			$("#ui_box_confirm").fadeOut("fast");
			$("#ui_box_confirm").remove();
			if ("undefined" == typeof(callback)) {
				return true
			} else {
				if ("function" == typeof(callback)) {
					callback()
				} else {
					eval(callback)
				}
			}
		});
		return false
	},
	confirmBox: function(title, text, callback) {
		this.box.init(title);
		text = text || L("PUBLIC_ACCONT_TIPES");
		text = "<i class='aicon-exclamation-sign'></i>" + text;
		var content = '<div class="pop-create-group"><dl><dt class="txt">' + text + '</dt><dd class="action"><a class="btn btn-blue mr10" href="javascript:void(0)"><span>' + L("PUBLIC_QUEDING") + '</span></a><a class="btn btn-gray" href="javascript:void(0)"><span>' + L("PUBLIC_QUXIAO") + "</span></a></dd></dl></div>";
		this.box.setcontent(content);
		this.box.center();
		var _this = this;
		$("#uibox .btn-gray").one("click",
		function() {
			$("#uibox").fadeOut("fast",
			function() {
				$("#uibox").remove()
			});
			_this.box.close();
			return false
		});
		$("#uibox .btn-blue").one("click",
		function() {
			$("#uibox").fadeOut("fast",
			function() {
				$("#uibox").remove()
			});
			_this.box.close();
			if ("undefined" == typeof(callback)) {
				return true
			} else {
				if ("function" == typeof(callback)) {
					callback()
				} else {
					eval(callback)
				}
			}
		});
		return false
	},
	box: {
		WRAPPER: '<table class="M-layer div-focus" id="uibox" cellspacing="0" cellpadding="0" style="display:none"><tbody><tr><td><div class="M-content"><div class="bd" id="layer-content"></div></div></td></tr></tbody></table>',
		simple_box: '<table class="M-layer div-focus" id="uibox" cellspacing="0" cellpadding="0" style="display:none"><tbody><tr><td id="layer-content"></td></tr></tbody></table>',
		inited: false,
		IE6: (jQuery.browser.msie && jQuery.browser.version < 7),
		init: function(d, e, c) {
			this.callback = e;
			core.facecard.dohide();
			if ($("#uibox").length > 0) {
				return false
			} else {
				if ("undefined" == typeof(d) || d == "") {
					$("body").prepend(this.simple_box)
				} else {
					$("body").prepend(this.WRAPPER)
				}
			}
			if ("undefined" != typeof(d) || d == "") {
				$("<div class='hd'><span class='left'>" + d + "</span><a class='ico-close' href='javascript:;'></a></div>").insertBefore($("#uibox .bd"));
				$(".hd").mousedown(function() {
					$(".mod-at-wrap").remove()
				});
				$("#uibox").find(".ico-close").click(function() {
					ui.box.close(e);
					return false
				})
			}
			ui.showblackout();
			$("#uibox").stop().css({
				width: "",
				height: ""
			});
			var b = $.browser.opera ? "keypress": "keydown";
			$(document.body).bind(b + ".uibox",
			function(g) {
				var f = g.keyCode ? g.keyCode: g.which ? g.which: g.charCode;
				if (f == 27) {
					$(document.body).unbind(b + ".uibox");
					ui.box.close(e);
					return false
				}
			});
			this.center();
			if ("undefined" == typeof(c)) {
				var a = function() {
					$("#uibox").show()
				};
				setTimeout(a, 200)
			}
		},
		setcontent: function(a) {
			$("#layer-content").html(a)
		},
		close: function(fn) {
			$("#ui-fs .ui-fs-all .ui-fs-allinner div.list").find("a").die("click");
			$("#uibox").remove();
			$(".mod-at-wrap").remove();
			$("#recipientsTips").hide();
			jQuery(".boxy-modal-blackout").remove();
			if ($("#rcalendar").length > 0) {
				$("#rcalendar").hide()
			}
			var back = "";
			if ("undefined" != typeof(fn)) {
				back = fn
			} else {
				if ("undefined" != typeof(this.callback)) {
					back = this.callback
				}
			}
			if ("function" == typeof(back)) {
				back()
			} else {
				eval(back)
			}
		},
		alert: function(a, b, c) {
			this.init(b, c);
			this.setcontent('<div class="question">' + a + "</div>");
			this.center()
		},
		show: function(a, b, c) {
			this.init(b, c);
			this.setcontent(a);
			this.center()
		},
		showImage: function(a, d) {
			this.init();
			var c = a.split("|");
			if (c.length > 1) {
				a = c[0];
				var b = c[1];
				var h = c[2]
			} else {
				a = a;
				var b = 0;
				var h = 0
			}
			if ("undefined" == typeof(d) || d == "") {
				d = a
			}
			var e = function(r, m, k) {
				if (m == 0 || k == 0) {
					var o = new Image();
					o.src = r;
					m = o.width;
					k = o.height
				}
				var n = {
					width: 0,
					height: 0
				};
				var l = screen.width;
				var q = screen.height;
				var p = Math.min((l * 0.6) / m, (q * 0.6) / k);
				if (p >= 1) {
					n.width = m;
					n.height = k
				} else {
					n.width = parseInt(m * p);
					n.height = parseInt(k * p)
				}
				return n
			};
			var j = e(d, b, h);
			if (j.width == 0 || j.height == 0) {
				var g = ""
			} else {
				var g = 'width="' + j.width + '" height="' + j.height + '"'
			}
			content = '<div class="layer-close"><a class="ico-close-white" href="javascript:;"></a></div><div class="pop-show-img"><dl><dd><img src="' + a + '" ' + g + '/></dd><dd class="img-info"><a href="' + d + '" target="_blank" class="qg-link">' + L("PUBLIC_LOOK_SOURCE") + "</a></dd></dl></div>";
			this.setcontent(content);
			if (g == "") {
				var f = this;
				setTimeout(function() {
					f.center()
				},
				250)
			} else {
				this.center()
			}
			$("#uibox").find(".ico-close-white").click(function() {
				ui.box.close();
				return false
			})
		},
		showDoc: function(a) {
			this.init();
			content = '<div class="pop-file-img"><div class="layer-close" ><a class="ico-close-white" href="javascript:;"></a></div><div><dl><dd class="center">' + a.html + '</a><a class="qg-link-bg"></a></dd><dd class="file-img-info"><a href="' + a.url + '" class="right" >' + a.tips + "</a>" + a.name + " <span>(" + a.size + ")</span></dd></dl></div></div>";
			this.setcontent(content);
			this.center();
			$("#uibox").find(".ico-close-white").click(function() {
				ui.box.close();
				return false
			})
		},
		removeReader: function() {
			$(".reader-box-container").fadeOut();
			$("#topcontrol").show();
			$("#autoTalkBox").show();
			$("#page-wrap").show()
		},
		showReader: function(d) {
			if ($(".reader-box-container").length > 0) {
				$("#topcontrol").hide();
				$("#autoTalkBox").hide();
				$("#page-wrap").hide();
				$(".reader-box-container").fadeIn();
				return false
			}
			var c = $(window).width();
			var a = $(window).height();
			var b = '<div class="reader-box-container" style="width:' + c + "px;width:100%;height:" + a + 'px;top:0;left:0;display:none;overflow-y:auto;"><div class="reader-box"><div class="reader-box-body"><div class="reader-pop"><div class="reader-close"><a href="javascript:;" class="ico-close" onclick="ui.box.removeReader()"></a></div><dl><dt class="reader-font-zoom"><span class="font-samll" >A</span><i class="vline">|</i><span class="font-big">A</span></dt><dd id="readBox" size="16" style="font-size:16px;">' + d + "</dd></dl></div></div></div></div>";
			$("body").append(b);
			if ($("body").height() < $(window).height() + 50) {
				$(".reader-pop").css("height", $(window).height() - 102 + "px")
			} else {
				$(".reader-pop").css("min-height", $(window).height() - 102 + "px");
				$(".reader-pop").css("height", "auto")
			}
			$("#topcontrol").hide();
			$("#autoTalkBox").hide();
			$("#page-wrap").hide();
			$(".reader-box-container").fadeIn();
			$(".font-big").click(function() {
				if ($("#readBox").attr("size") < 40) {
					var e = parseInt($("#readBox").attr("size")) + 2;
					$("#readBox").attr("size", e);
					$("#readBox *").css("font-size", e + "px")
				}
			});
			$(".font-samll").click(function() {
				if ($("#readBox").attr("size") > 12) {
					var e = parseInt($("#readBox").attr("size")) - 2;
					$("#readBox").attr("size", e);
					$("#readBox *").css("font-size", e + "px")
				}
			})
		},
		load: function(c, f, g, a, b) {
			this.init(f, g);
			if ("undefined" != typeof(b)) {
				var e = b
			} else {
				var e = "GET"
			}
			this.setcontent('<div style="width:150px;height:70px;text-align:center"><div class="load">&nbsp;</div></div>');
			var d = this;
			if ("undefined" == a) {
				var a = {}
			}
			jQuery.ajax({
				url: c,
				type: e,
				data: a,
				cache: false,
				dataType: "html",
				success: function(h) {
					d.setcontent(h);
					d.center();
					setTimeout(function() {
						$("#uibox").find('input[type="text"]').eq(0).focus()
					},
					100)
				}
			})
		},
		_viewport: function() {
			var g = document.documentElement,
			c = document.body,
			e = window;
			var f = e.pageYOffset > 0 ? e.pageYOffset: 0;
			var a = c.scrollTop || g.scrollTop;
			if (a < 0) {
				a = 0
			}
			return jQuery.extend(jQuery.browser.msie ? {
				left: c.scrollLeft || g.scrollLeft,
				top: a
			}: {
				left: e.pageXOffset,
				top: f
			},
			!ui.box._u(e.innerWidth) ? {
				width: e.innerWidth,
				height: e.innerHeight
			}: (!ui.box._u(g) && !ui.box._u(g.clientWidth) && g.clientWidth != 0 ? {
				width: g.clientWidth,
				height: g.clientHeight
			}: {
				width: c.clientWidth,
				height: c.clientHeight
			}))
		},
		_u: function() {
			for (var a = 0; a < arguments.length; a++) {
				if (typeof arguments[a] != "undefined") {
					return false
				}
			}
			return true
		},
		_cssForOverlay: function() {
			if (ui.box.IE6) {
				return ui.box._viewport()
			} else {
				return {
					width: "100%",
					height: jQuery(document).height()
				}
			}
		},
		center: function(d) {
			$("body").unbind("click", drop_clayer);
			$("body").bind("click", drop_clayer);
			var c = document.body,
			a = document.documentElement,
			e = c.scrollTop > 0 ? c.scrollTop: a.scrollTop;
			var b = ui.box._viewport();
			var f = [b.left, e];
			if (!d || d == "x") {
				this.centerAt(f[0] + b.width / 2, null)
			}
			if (!d || d == "y") {
				this.centerAt(null, f[1] + b.height / 2)
			}
			return this
		},
		moveToX: function(a) {
			if (typeof a == "number") {
				$("#uibox").css({
					left: a
				})
			} else {
				this.centerX()
			}
			return this
		},
		moveToY: function(a) {
			if (typeof a == "number") {
				if (a <= 0) {
					a = 0
				}
				$("#uibox").css({
					top: a
				})
			} else {
				this.centerY()
			}
			return this
		},
		centerAt: function(a, c) {
			var b = this.getSize();
			if (typeof a == "number") {
				this.moveToX(a - b[0] / 2)
			}
			if (typeof c == "number") {
				this.moveToY(c - b[1] / 2)
			}
			return this
		},
		centerAtX: function(a) {
			return this.centerAt(a, null)
		},
		centerAtY: function(a) {
			return this.centerAt(null, a)
		},
		getSize: function() {
			return [$("#uibox").width(), $("#uibox").height()]
		},
		getContent: function() {
			return $("#uibox").find(".boxy-content")
		},
		getPosition: function() {
			var a = $("#uibox");
			return [a.offsetLeft, a.offsetTop]
		},
		getContentSize: function() {
			var a = this.getContent();
			return [a.width(), a.height()]
		},
		_getBoundsForResize: function(c, a) {
			var b = this.getContentSize();
			var e = [c - b[0], a - b[1]];
			var d = this.getPosition();
			return [Math.max(d[0] - e[0] / 2, 0), Math.max(d[1] - e[1] / 2, 0), c, a]
		}
	}
}; (function() {
	var e = {
		boxID: "autoTalkBox",
		valuepWrap: "autoTalkText",
		wrap: "recipientsTips",
		listWrap: "autoTipsUserList",
		position: "autoUserTipsPosition",
		positionHTML: '<span id="autoUserTipsPosition">&nbsp;123</span>',
		className: "autoSelected"
	};
	var f = '<div id="autoTalkBox" style="text-align:left;z-index:1;top:$top$px;left:$left$px;width:$width$px;height:$height$px;position:absolute;scroll-top:$SCTOP$px;overflow:hidden;overflow-y:auto;visibility:hidden;word-break:break-all;word-wrap:break-word;"><span id="autoTalkText" style="font-size:14px;margin:0;"></span></div><div id="recipientsTips" class="recipients-tips div-focus"><div class="recipients"><h4>$FORMAT$</h4><ul id="autoTipsUserList"></ul></div></div>';
	var c = {
		user: '<li style="text-align:left" class=""><a rel="$ID$" relid="$RELID$" >@$SACCOUNT$</a></li>',
		topic: '<li style="text-align:left" class=""><a rel="$ID$" relid="$RELID$" >$SACCOUNT$</a></li>',
		flag: '<li style="text-align:left" class=""><a rel="$ID$" relid="$RELID$" >$SACCOUNT$</a></li>'
	};
	var d = {
		user: /(\w+)?@(\S+)$|@$/,
		topic: /(\w+)?[#＃]([^\s|^#|^＃]+)$/,
		flag: /(\w+)?[｛{](\S+)$|[{｛]$/
	};
	var a = {
		$: function(k) {
			return document.getElementById(k)
		},
		DC: function(k) {
			return document.createElement(k)
		},
		EA: function(l, k, n, m) {
			if (l.addEventListener) {
				if (k == "mousewheel") {
					k = "DOMMouseScroll"
				}
				l.addEventListener(k, n, m);
				return true
			} else {
				return l.attachEvent ? l.attachEvent("on" + k, n) : false
			}
		},
		ER: function(l, k, m) {
			if (l.removeEventListener) {
				l.removeEventListener(k, m, false);
				return true
			} else {
				return l.detachEvent ? l.detachEvent("on" + k, m) : false
			}
		},
		BS: function() {
			var l = document.body,
			k = document.documentElement,
			m = l.scrollTop + k.scrollTop;
			left = l.scrollLeft + k.scrollLeft;
			return {
				top: m,
				left: left
			}
		},
		FF: (function() {
			var k = navigator.userAgent.toLowerCase();
			return /firefox\/([\d\.]+)/.test(k)
		})()
	};
	var b = {
		info: function(l) {
			var p = l.getBoundingClientRect();
			var k = l.offsetWidth;
			var n = l.offsetHeight;
			var m = l.style;
			return {
				top: p.top,
				left: p.left,
				width: k,
				height: n,
				style: m
			}
		},
		getCursorPosition: function(m) {
			if (document.selection) {
				m.focus();
				var n = document.selection;
				var k = null;
				k = n.createRange();
				var l = k.duplicate();
				l.moveToElementText(m);
				l.setEndPoint("EndToEnd", k);
				m.selectionStart = l.text.length - k.text.length;
				m.selectionEnd = m.selectionStart + k.text.length;
				return m.selectionStart
			} else {
				return m.selectionStart
			}
		},
		setCursorPosition: function(l, m) {
			var o = m == "end" ? l.value.length: m;
			if (document.selection) {
				var k = l.createTextRange();
				k.moveEnd("character", -l.value.length);
				k.moveEnd("character", o);
				k.moveStart("character", o);
				k.select()
			} else {
				l.setSelectionRange(o, o);
				l.focus()
			}
		},
		add: function(l, k) {
			var o = l.value;
			var m = m || "";
			if (document.selection) {
				document.selection.createRange().text = k
			} else {
				var n = l.selectionStart;
				var p = l.value.length;
				l.value = l.value.slice(0, l.selectionStart) + k + l.value.slice(l.selectionStart, p);
				this.setCursorPosition(l, n + k.length)
			}
		},
		del: function(k, o) {
			var m = this.getCursorPosition(k);
			var l = k.scrollTop;
			k.value = k.value.slice(0, m - o) + k.value.slice(m);
			this.setCursorPosition(k, m - o);
			a.FF && setTimeout(function() {
				k.scrollTop = l
			},
			10)
		}
	};
	var j = {
		inquiry: function(n, p, k) {
			var m = new RegExp(p, "i");
			var l = 0;
			var o = [];
			while (o.length < k && l < n.length) {
				if (m.test(n[l]["user"])) {
					o.push(n[l])
				}
				l++
			}
			return o
		}
	};
	var h = function() {
		this._this = null;
		this.index = -1;
		this.list = null
	};
	h.prototype = {
		selectIndex: function(m) {
			if (a.$(e.wrap).style.display == "none") {
				return true
			}
			var k = this.index;
			switch (m) {
			case 40:
				k = k + 1;
				break;
			case 38:
				k = k - 1;
				break;
			case 13:
				return this._this.enter();
				break;
			case 32:
				return this._this.enter();
				break
			}
			var l = $("#" + e.listWrap).find("lI").size();
			k = k >= l ? 0 : k < 0 ? l - 1 : k;
			$("#" + e.listWrap).find("LI").removeClass(e.className);
			$("#" + e.listWrap).find("LI").eq(k).addClass(e.className);
			return this.setSelected(k)
		},
		setSelected: function(k) {
			var l = this;
			if (k == 0) {
				this.list.find("LI").each(function(m) {
					if (m == k) {
						this.className = e.className
					} else {
						this.className = ""
					}
				})
			}
			l.index = k;
			return false
		}
	};
	var g = function(k) {
		this.elem = k.elem;
		this.mod = k.mod;
		this.url = k.url;
		this.checkLength = 10;
		this.key = "";
		this.lastkey = "-";
		this.selectList = new h();
		this.tips_user = "undefined" == typeof(k.tips) ? L("PUBLIC_SELECT_AT_USER") : k.tips;
		this.tips_topic = "undefined" == typeof(k.tips) ? L("PUBLIC_SELECT_POST_TOPIC") : k.tips;
		this.tips_flag = "undefined" == typeof(k.tips) ? L("PUBLIC_PLEASE_INPUT_CONTENT") : k.tips;
		this.callback = "undefined" == typeof(k.callback) ? "": k.callback
	};
	g.prototype = {
		start: function() {
			if ($("#" + e.boxID).length < 1) {
				var l = f.slice();
				var n = b.info(this.elem);
				var k = a.BS();
				l = l.replace("$top$", (n.top + k.top)).replace("$left$", (n.left + k.left)).replace("$width$", n.width).replace("$height$", n.height).replace("$SCTOP$", "0");
				var m = $(l);
				$("body").append(m)
			} else {
				this.updatePosstion()
			}
		},
		bind: function() {
			var p = this;
			var n = function(q) {
				return p.keyupFn(q, "user")
			};
			var l = function(q) {
				return p.keyupFn(q, "topic")
			};
			var m = function(q) {
				return p.keyupFn(q, "flag")
			};
			var k = function(r, q) {
				jQuery(r.elem).bind("keyup", q);
				if ("undefined" == typeof(r.elem.onkeyup)) {
					jQuery(r.elem).bind("click", q);
					if (navigator.userAgent.indexOf("MSIE") == -1) {
						r.elem.oninput = q
					}
				}
			};
			if (this.mod == "user") {
				var o = n
			} else {
				if (this.mod == "topic") {
					var o = l
				} else {
					var o = m
				}
			}
			k(this, o);
			$("body").live("click",
			function() {
				p.hide(p)
			})
		},
		keyupFn: function(o, r) {
			var n = this;
			var o = o || window.event;
			var k = o.keyCode;
			if (k == 38 || k == 40 || k == 13) {
				if (k == 13 && a.$(e.wrap).style.display != "none") {}
				return false
			}
			var q = b.getCursorPosition(n.elem);
			if (!q) {
				return n.hide(n)
			}
			var t = n.elem.value.slice(0, q);
			var l = t.slice( - n.checkLength);
			var m = l.charAt(l.length - 1);
			if (n.mod == "user" && m == " ") {
				n.hide(n);
				return false
			}
			if (n.mod == "topic" && (m == "#" || m == "＃")) {
				if ($("#" + e.wrap).is(":visible")) {
					n.hide(n);
					return false
				}
			}
			var p = l.match(d[r]);
			if (p == null) {
				return false
			} else {
				var s = p[2] ? p[2] : ""
			}
			a.$(e.valuepWrap).innerHTML = t.slice(0, t.length - s.length).replace(/\n/g, "<br/>").replace(/\s/g, "&nbsp;") + e.positionHTML;
			n.showList(s, n)
		},
		updatePosstion: function() {
			var l = b.info(this.elem);
			var k = a.BS();
			var m = a.$(e.boxID).style;
			m.top = l.top + k.top + "px";
			m.left = l.left + k.left + "px";
			m.width = l.width + "px";
			m.height = l.height + "px";
			a.$(e.boxID).scrollTop = this.elem.scrollTop
		},
		hide: function(k) {
			this.selectList.list = null;
			this.selectList.index = -1;
			this.selectList._this = null;
			a.ER(k.elem, "keydown", k.KeyDown);
			$("#" + e.wrap).fadeOut("fast");
			$("#" + e.listWrap).html("")
		},
		KeyDown: function(m, l) {
			var m = m || window.event;
			var k = m.keyCode;
			if (k == 38 || k == 40 || k == 13) {
				return l.selectList.selectIndex(k)
			}
			return true
		},
		showList: function(k, l) {
			if (l.lastkey == k && k != "") {
				return
			}
			if (k == "" && l.mod == "topic") {
				return
			}
			l.key = k;
			l.lastkey = k;
			$.get(l.url, {
				key: l.key
			},
			function(r) {
				if (r.status == 0 || r.data == null) {
					if (l.mod == "topic") {
						var u = "";
						l.updatePosstion();
						var o = a.$(e.position).getBoundingClientRect();
						var y = a.BS();
						var x = a.$(e.wrap).style;
						x.top = o.top + 20 + y.top + "px";
						x.left = o.left - 5 + "px";
						$("#" + e.listWrap).html(u);
						l.show(l);
						l.lastkey = ""
					}
					return
				}
				var t = r.data;
				var v = c[l.mod].slice();
				var u = "";
				var w = t.length;
				if (w == 0) {
					l.hide(l);
					return
				}
				var q = new RegExp(k);
				var n = "<em>" + k + "</em>";
				for (var s = 0; s < w; s++) {
					if (l.mod == "user") {
						var m = t[s]["name"].replace(q, n) + "<span>" + t[s]["note"] + "</span>";
						var z = "undefined" == typeof(t[s]["rel"]) ? t[s]["name"] : t[s]["rel"];
						u += v.replace("$SACCOUNT$", m).replace("$ID$", z).replace("$AVATAR$", t[s]["img"]).replace("$RELID$", t[s]["id"])
					} else {
						if (l.mod == "topic") {
							var m = t[s]["topic_name"].replace(q, n);
							var z = "undefined" == typeof(t[s]["rel"]) ? t[s]["topic_name"] : t[s]["rel"];
							u += v.replace("$SACCOUNT$", m).replace("$ID$", z).replace("$RELID$", t[s]["topic_id"])
						} else {
							var m = t[s]["name"].replace(q, n);
							var z = "undefined" == typeof(t[s]["rel"]) ? t[s]["name"] : t[s]["rel"];
							u += v.replace("$SACCOUNT$", m).replace("$ID$", z).replace("$RELID$", 1)
						}
					}
				}
				l.updatePosstion();
				var o = a.$(e.position).getBoundingClientRect();
				var y = a.BS();
				var x = a.$(e.wrap).style;
				x.top = o.top + 20 + y.top + "px";
				x.left = o.left - 5 + "px";
				$("#" + e.listWrap).html(u);
				l.show(l);
				l.lastkey = ""
			},
			"json")
		},
		show: function(l) {
			l.selectList.list = $("#" + e.listWrap);
			l.selectList.index = -1;
			l.selectList._this = l;
			l.elem.onkeydown = function(m) {
				return l.KeyDown(m, l)
			};
			l.selectList.setSelected(0);
			if (l.mod == "user") {
				$("#" + e.wrap).find("h4").html(this.tips_user)
			} else {
				if (l.mod == "topic") {
					$("#" + e.wrap).find("h4").html(this.tips_topic)
				} else {
					$("#" + e.wrap).find("h4").html(this.tips_flag)
				}
			}
			var k = $("#" + e.wrap).html();
			$("#" + e.wrap).html(k);
			$("#" + e.wrap).fadeIn("fast");
			$("#" + e.wrap).find("LI").each(function(m) {
				var n = this;
				this.onmouseover = function() {
					l.selectList.setSelected(m);
					$("#" + e.wrap).find("LI").removeClass(e.className);
					$(n).addClass(e.className)
				};
				this.onclick = function() {
					l.enter()
				}
			})
		},
		enter: function() {
			if (this.selectList.list == null) {
				return false
			}
			b.del(this.elem, this.key.length, this.key);
			var k = $("#" + e.listWrap).find("a").eq(this.selectList.index).attr("rel");
			if ("undefined" == typeof(k)) {
				k = this.key
			}
			if (this.mod == "user") {
				b.add(this.elem, k + " ")
			} else {
				if (this.mod == "topic") {
					b.add(this.elem, k + "# ")
				} else {
					b.add(this.elem, k + "} ")
				}
			}
			if (this.callback != "") {
				var l = {};
				l.name = $(".autoSelected").find("a").attr("rel");
				l.id = $(".autoSelected").find("a").attr("relid");
				l.note = $(".autoSelected").find("a").find("span").html();
				this.callback(l)
			}
			this.hide(this);
			return false
		}
	};
	window.setAutoTips = function(k) {
		setTimeout(function() {
			var l = new g(k);
			l.start();
			l.bind()
		},
		101)
	}
})();
setAutoTipsDom = function(d) {
	$("#recipientsTips_editor").remove();
	d.editor.insertHtml('<label class="tempspan"></label>' + d.mod);
	var h = $(d.elem).find(".tempspan").offset();
	var u = d.parent.offset();
	var l = d.mod == "@" ? L("PUBLIC_SELECT_AT_USER") : L("PUBLIC_SELECT_POST_TOPIC");
	var c = "undefined" == typeof(d.tips) ? l: d.tips;
	var p = d.editor.fullscreenMode ? (h.top + 30) : h.top + u.top + 30;
	var e = d.editor.fullscreenMode ? (h.left + 13) : h.left + u.left + 13;
	var q = getScrollTop(d.editor.edit.doc, d.editor.edit.win);
	p -= q;
	if (navigator.userAgent.indexOf("Firefox") >= 0) {
		e += 2
	} else {
		p += 15
	}
	var n = '<div id="recipientsTips_editor" class="recipients-tips div-focus" style="top:' + p + "px;left:" + e + 'px;display:block;"><input type="text" id="setKeyInput"><div class="recipients"><h4>' + c + '</h4><ul id="autoTipsUserList_editor"></ul></div></div>';
	$("body").append(n);
	$("#setKeyInput").focus();
	$("#recipientsTips_editor").bind("keyup",
	function(x) {
		var x = x || window.event;
		var w = x.keyCode;
		if (w == 38) {
			g( - 1);
			return false
		} else {
			if (w == 40) {
				g(1);
				return false
			} else {
				if (w == 13) {
					a();
					return false
				} else {
					if (w == 32) {
						if ($("#setKeyInput").length > 0) {
							var t = $("#setKeyInput").val();
							if (t.indexOf(" ") > 0) {
								a();
								return false
							}
						}
					}
				}
			}
		}
	});
	$("body").not("#recipientsTips_editor").bind("click",
	function() {
		j()
	});
	var b = 1;
	var m = "-1";
	var s = "";
	var k = function() {
		if ($("#recipientsTips_editor").length > 0) {
			var v = $("#recipientsTips_editor input").val();
			if (v != m) {
				r(v)
			}
			m = v;
			var t = d.elem.innerHTML.match(/<label class="tempspan"><\/label>[@|#|＃](\S+)$/i);
			if (t == null && $(d.elem).find(".tempspan").length < 1) {
				j()
			}
		}
	};
	var o = setInterval(k, 200);
	var r = function(w) {
		var t = b;
		var v = d.mod == "@" ? U("widget/Search/searchUser", {
			follow: 0,
			limit: 10,
			group: 1
		}) : U("widget/Topic/search");
		$.post(v, {
			key: w
		},
		function(x) {
			if (t == b) {
				f(x);
				b++
			}
		},
		"json")
	};
	var g = function(t) {
		var v = $(".autoSelected");
		if (v.length > 0) {
			v.removeClass("autoSelected");
			if (t == 1) {
				if (v.next().length > 0) {
					v.next().addClass("autoSelected")
				} else {
					$("#autoTipsUserList_editor li:first").addClass("autoSelected")
				}
			} else {
				if (v.prev().length > 0) {
					v.prev().addClass("autoSelected")
				} else {
					$("#autoTipsUserList_editor li:last").addClass("autoSelected")
				}
			}
		}
	};
	var j = function() {
		clearInterval(o);
		$("#recipientsTips_editor").unbind("keyup");
		$("#recipientsTips_editor").remove();
		$(d.elem).find(".tempspan").remove()
	};
	var a = function() {
		var v = $(".autoSelected").find("a");
		if (typeof(v.attr("rel")) != "undefined") {
			if (d.mod == "@") {
				var t = v.attr("rel") + " &nbsp;"
			} else {
				var t = v.attr("rel") + "# "
			}
			d.editor.insertHtml(t);
			d.select(v.attr("rel"))
		} else {
			var t = (d.mod != "@" && $("#setKeyInput").val() != "") ? $("#setKeyInput").val() + "# ": $("#setKeyInput").val() + "&nbsp;";
			d.editor.insertHtml(t)
		}
		j()
	};
	var f = function(v) {
		if (v.status == 1) {
			if (v.data.length > 0) {
				var y = "";
				for (var x in v.data) {
					var w = x == 0 ? 'class="autoSelected"': "";
					if (d.mod == "@") {
						var t = "undefined" == typeof(v.data[x].rel) ? v.data[x].name: v.data[x].rel;
						y += "<li " + w + " style='text-align:left'><a rel='" + t + "'>@<em></em>" + v.data[x].name + "<span>" + v.data[x].note + "</span></a></li>"
					} else {
						var t = "undefined" == typeof(v.data[x].rel) ? v.data[x].topic_name: v.data[x].rel;
						y += "<li " + w + " style='text-align:left'><a rel='" + t + "'>" + v.data[x].topic_name + "</a></li>"
					}
				}
				$("#autoTipsUserList_editor").html(y);
				$("#autoTipsUserList_editor").find("li").each(function() {
					$(this).mouseover(function() {
						$(this).addClass("autoSelected");
						$(this).siblings().removeClass("autoSelected")
					});
					$(this).click(function() {
						$(this).addClass("autoSelected");
						$(this).siblings().removeClass("autoSelected");
						a()
					})
				})
			} else {
				$("#autoTipsUserList_editor").html("")
			}
		} else {
			$("#autoTipsUserList_editor").html("")
		}
	};
	return {
		search: function(t) {
			r(t)
		},
		clean: function() {
			j()
		},
		select: function() {
			a()
		},
		move: function(t) {
			g(t)
		}
	}
};
setAutoTipsEditor = function(b, c) {
	var a = {
		clean: function() {},
		search: function() {},
		select: function() {},
		move: function() {}
	};
	$(b.edit.doc.body).bind("keydown",
	function(g) {
		$(b.edit.doc.body).find("#xunlei_com_thunder_helper_plugin_d462f475-c18e-46be-bd10-327458d045bd").each(function() {
			$(this).replaceWith($(this).html())
		});
		var g = g || window.event;
		var f = g.keyCode;
		if (f == 50 && g.shiftKey) {
			a.clean();
			a = setAutoTipsDom({
				elem: b.edit.doc.body,
				parent: $("#" + c).parent(),
				mod: "@",
				url: U("widget/Search/searchUser", {
					follow: 0
				}),
				editor: b,
				select: function(e) {}
			});
			return false
		} else {
			if (f == 51 && g.shiftKey) {
				a.clean();
				a = setAutoTipsDom({
					elem: b.edit.doc.body,
					parent: $("#" + c).parent(),
					mod: "#",
					url: U("widget/Topic/search"),
					editor: b,
					select: function(e) {}
				});
				return false
			} else {
				if (f == 38 && $("#recipientsTips_editor").is(":visible")) {
					a.move( - 1);
					return false
				} else {
					if (f == 40 && $("#recipientsTips_editor").is(":visible")) {
						a.move(1);
						return false
					} else {
						if (f == 13 && $("#recipientsTips_editor").is(":visible")) {
							a.select();
							return false
						} else {
							if (f == 32) {
								if ($("#setKeyInput").length > 0) {
									var d = $("#setKeyInput").val();
									if (d.indexOf(" ") > 0) {
										a.clean();
										return false
									}
								}
							}
						}
					}
				}
			}
		}
	})
};
$(function() {
	$(".slider_div").each(function() {
		var e = $(this).find(".slider_mod");
		var c = $(this).find("li");
		var d = Math.round(e.width() / c.width());
		var b = $(this).find(".prev");
		var a = $(this).find(".next");
		core.plugFunc("imgshow",
		function() {
			core.imgshow.slider(e, c.size(), d, b, a)
		})
	})
});
var getScrollTop = function(b, a) {
	if ("undefined" == typeof(b)) {
		var b = document
	}
	if ("undefined" == typeof(a)) {
		var a = window
	}
	return ! ("pageYOffset" in a) ? (b.compatMode === "BackCompat") ? b.body.scrollTop: b.documentElement.scrollTop: a.pageYOffset
};
var bindDateSelect = function() {
	$("div").not(".rcalendar").bind("click", _bindDateSelect)
};
var _bindDateSelect = function(a) {
	if ($(a.target).hasClass("rcalendar_input")) {
		return false
	}
	if ($(this).closest(".rcalendar").length > 0) {
		return false
	}
	if ($("#rcalendar").length > 0) {
		$("#rcalendar").hide()
	}
};
var unbindDateSelect = function() {
	$("div").not(".rcalendar").unbind("click", _bindDateSelect)
};
var bindSearchUser = function() {
	var a = new CoreSearchUser();
	$(".choose-user").each(function() {
		var c = $(this).attr("max");
		var e = $(this).attr("noself");
		var d = $(this).attr("callback");
		var b = $(this).attr("follow");
		$(this).find("input").each(function() {
			if ($(this).attr("event-node") == "search_user") {
				a.init($(this), b, c, d, e);
				a._stopUser();
				$(this).parent().find(".ico-close").click(function() {
					a.removeUser($(this).attr("uid"), this, $(this).attr("inputname"))
				});
				$(this).click(function() {
					if (this.value == $(this).attr("defaultValue")) {
						this.value = ""
					}
					a.init($(this), b, c, d, e)
				});
				$(this).blur(function() {
					if (this.value == "" || this.value == $(this).attr("defaultValue")) {
						this.value = $(this).attr("defaultValue");
						a.inputReset()
					}
				});
				$(this).focus(function() {
					if (this.value == $(this).attr("defaultValue")) {
						this.value = ""
					}
					a.init($(this), b, c, d, e)
				})
			}
		})
	})
};
$(function() {
	$("#buy-advisory").click(function() {
		$("#pop-online dd").animate({
			width: "+200px"
		},
		300)
	});
	$("#pop-online dt").click(function() {
		var a = $("#pop-online dd").width();
		if (a > 0) {
			$("#pop-online dd").animate({
				width: "-=200px"
			},
			300)
		} else {
			$("#pop-online dd").animate({
				width: "+=200px"
			},
			300)
		}
	})
});
$(function() {
	$.fn.extend({
		nav: function(d) {
			var j = parseInt(d.n);
			var h = $(this),
			b = h.find(".switch-tab"),
			f = (d && d.t) || 3000,
			c = (d && d.a) || 500,
			e = 0,
			g = function() {
				b.find("a:eq(" + (e + 1 === j ? 0 : e + 1) + ")").addClass("current").siblings().removeClass("current");
				h.find(".event-item:eq(" + e + ")").css("display", "none").end().find(".event-item:eq(" + (e + 1 === j ? 0 : e + 1) + ")").css({
					display: "block",
					opacity: 1
				}).animate({
					opacity: 1
				},
				c,
				function() {
					e = e + 1 === j ? 0 : e + 1
				}).siblings(".event-item").css({
					display: "none",
					opacity: 1
				})
			};
			h.hover(function() {
				return false
			},
			function() {
				return false
			}).find(".switch-nav>a").bind("click",
			function() {
				var a = b.find(".current").index();
				if ($(this).attr("class") === "prev") {
					e = a == 0 ? j - 2 : a - 2
				} else {
					e = a
				}
				g();
				return false
			});
			return h
		}
	})
});
function changeSpace(g, c, d) {
	if ($config.app_name == "store") {
		var f = {
			space_name: g,
			app_name: c
		};
		var a = c == "" ? U("store/Index/index", f, 0) : U("store/Index/detail", f, 0);
		location.href = a
	} else {
		var b = $("#drop_li_a").attr("href");
		var e = $("#drop_li_a").attr("space_name");
		$("#drop_li_a").attr("href", $(d).attr("linkurl"));
		$("#drop_li_a").attr("space_name", $(d).attr("space_name"));
		$("#drop_div").html($(d).html() + '<i class="ico-arrow-down"></i>');
		$(".drop").removeClass("open");
		$(d).attr("linkurl", b);
		$(d).attr("space_name", e);
		$(d).html(e)
	}
}
var drop_clayer = function(d) {
	var a = "undefined" != typeof(d.srcElement) ? d.srcElement: d.target;
	var e = null;
	if ($(a).hasClass(".drop_clayer")) {
		e = $(a)
	} else {
		if ($(a).closest(".drop_clayer").length > 0) {
			e = $(a).closest(".drop_clayer")
		}
	}
	var c = function(f) {
		if (f.parent().hasClass("drop")) {
			f.parent().addClass("open")
		}
	};
	var b = function(f) {
		if (f.parent().hasClass("drop")) {
			f.parent().removeClass("open")
		}
	};
	if (e == null) {
		setTimeout(function() {
			$(".drop_clayer").each(function() {
				if ($(a).closest($(this).parent()).length > 0) {
					if ($(this).parent().hasClass("open")) {
						$(this).show()
					}
				} else {
					$(this).hide();
					b($(this))
				}
			})
		},
		10)
	} else {
		$(".drop_clayer").hide()
	}
};
$(function() {
	$("body").bind("click", drop_clayer)
});
core.loading = function(a) {
	a.html("<div style='text-align:center;padding:10px;'><img src='" + THEME_URL + "/image/load.gif' ></div>")
};
$(function() {
	var d = $(".tab-animate .current");
	if (d.length > 0) {
		var c = d.innerWidth();
		var b = d.position().left;
		var a = $(".tab-animate-block");
		a.css({
			width: c,
			left: b
		});
		$(".tab-animate li").hover(function() {
			var j = $(this).width() * $(this).eq();
			var e = $(this).index();
			var g = $(".tab-animate").find("li").eq(e);
			var f = g.innerWidth();
			var h = g.position().left;
			a.stop().animate({
				width: f,
				left: h
			},
			300)
		},
		function() {
			a.stop().animate({
				width: c,
				left: b
			})
		})
	}
});
Date.prototype.yyyymmdd = function() {
	var c = this.getFullYear().toString();
	var b = (this.getMonth() + 1).toString();
	var a = this.getDate().toString();
	return c + "-" + (b[1] ? b: "0" + b[0]) + "-" + (a[1] ? a: "0" + a[0])
};
function inArray(c, d, a) {
	if (typeof c == "string" || typeof c == "number") {
		for (var b in d) {
			if (c === d[b]) {
				if (a) {
					return b
				}
				return true
			}
		}
		return false
	}
}
function getcookie(c) {
	var b = document.cookie.indexOf(c);
	var a = document.cookie.indexOf(";", b);
	return b == -1 ? "": unescape(document.cookie.substring(b + c.length + 1, (a > b ? a: document.cookie.length)))
}
function setcookie(g, f, e, d, b, c) {
	var a = new Date();
	a.setTime(a.getTime() + e);
	document.cookie = escape(g) + "=" + escape(f) + (a ? "; expires=" + a.toGMTString() : "") + (d ? "; path=" + d: "/") + (b ? "; domain=" + b: "") + (c ? "; secure": "")
};

(function(c) {
	var a = c.document;
	var b = function(e, d) {
		b.addFns(d);
		if (e) {
			b.nodes.init(e)
		}
	};
	b.addFns = function(d) {
		if (!d) {
			return b
		}
		if (d.model) {
			b.addModelFns(d.model)
		}
		if (d.event) {
			b.addEventFns(d.event)
		}
		return b
	};
	b.addModelFns = function(e) {
		if ("object" != typeof e) {
			return b
		}
		var d;
		for (d in e) {
			b.nodes.models.fns[d] = e[d]
		}
		return b
	};
	b.addEventFns = function(e) {
		if ("object" != typeof e) {
			return b
		}
		var d;
		for (d in e) {
			b.nodes.events.fns[d] = e[d]
		}
		return b
	};
	b.getArgs = function(d) {
		return d.getAttribute("model-node") ? b.getModelArgs(d) : b.getEventArgs(d)
	};
	b.setArgs = function(e, d) {
		return e.getAttribute("model-node") ? b.setModelArgs(e, d) : b.setEventArgs(e, d)
	};
	b.getModelArgs = function(e, d) {
		e.args || (e.args = b.URI2Obj(e.getAttribute("model-args")));
		return e.args
	};
	b.setModelArgs = function(e, d) {
		e.args = undefined;
		e.setAttribute("model-args", d);
		return b
	};
	b.getEventArgs = function(d) {
		d.args || (d.args = b.URI2Obj(d.getAttribute("event-args")));
		return d.args
	};
	b.setEventArgs = function(e, d) {
		e.args = undefined;
		e.setAttribute("event-args", d);
		return b
	};
	b.URI2Obj = function(g) {
		if (!g) {
			return {}
		}
		var h = {},
		f = g.split("&"),
		e,
		d;
		e = f.length;
		while (e-->0) {
			d = f[e];
			if (!d) {
				continue
			}
			d = d.split("=");
			h[d[0]] = d[1]
		}
		return h
	};
	b.getModels = function(d) {
		return b.nodes.models[d]
	};
	b.getEvents = function(d) {
		return b.nodes.events[d]
	};
	b.removeListener = function(d) {
		b.nodes.removeListener(d);
		return b
	};
	b.addListener = function(e, d) {
		b.nodes.addListener(e, d);
		return b
	};
	b.getPreviousModel = function(e, d) {
		return b.nodes.getPreviousModel(e, d)
	};
	b.getNextModel = function(e, d) {
		return b.nodes.getNextModel(e, d)
	};
	b.getPreviousEvent = function(e, d) {
		return b.nodes.getPreviousEvent(e, d)
	};
	b.getNextEvent = function(e, d) {
		return b.nodes.getNextEvent(e, d)
	};
	b.nodes = {
		init: function(d) {
			this._init(d);
			this._onload.execute();
			return this
		},
		_init: function(h, e) {
			var g = h.firstChild,
			f, d, i; ! e && (e = this.getParentModel(h));
			switch (h.nodeName) {
			case "DIV":
			case "UL":
			case "DL":
			case "FORM":
			case "LI":
			case "DD":
			case "TABLE":
			case "TR":
			case "P":
			case "TD":
			case "ARTICLE":
			case "SECTION":
			case "FIGURE":
				d = h.getAttribute("model-node");
				if (d) {
					this._clearModel(h);
					h.modelName = d;
					this.addListener(h, this.models.fns[d]);
					h.parentModel = e;
					if ("undefined" != typeof(e.childModels)) { (e.childModels[d] = e.childModels[d] || []).push(h)
					} (this.models[d] = this.models[d] || []).push(h);
					f = h
				}
				break;
			case "A":
			case "SPAN":
			case "LABEL":
			case "STRONG":
			case "INPUT":
			case "SELECT":
			case "BUTTON":
			case "IMG":
			case "TEXTAREA":
			case "H1":
			case "H2":
			case "H3":
			case "H4":
			case "I":
				i = h.getAttribute("event-node");
				if (i) {
					this._clearEvent(h);
					h.eventName = i;
					this.addListener(h, this.events.fns[i]);
					h.parentModel = e; (e.childEvents[i] = e.childEvents[i] || []).push(h); (this.events[i] = this.events[i] || []).push(h)
				}
				break;
			case "HEAD":
			case "BODY":
				this[h.nodeName.toLowerCase()] = h;
				break;
			case "#document":
				this._clearModel(h);
				break
			} ! f && (f = e);
			while (g) { (1 == g.nodeType) && this._init(g, f);
				g = g.nextSibling
			}
		},
		_clearModel: function(d) {
			d.modelName = undefined;
			d.parentModel = undefined;
			d.childModels = {};
			d.childEvents = {};
			d.args = undefined;
			return this
		},
		_clearEvent: function(d) {
			d.eventName = undefined;
			d.parentModel = undefined;
			d.args = undefined;
			return this
		},
		getParentModel: function(f) {
			var d = f.parentNode,
			e;
			if (d && 1 === d.nodeType) {
				e = d.getAttribute("model-node") ? d: this.getParentModel(d)
			}
			return e || a
		},
		getPreviousModel: function(e, d) {
			return this._getSiblingNode(e, {
				siblingType: "model",
				siblingName: d
			},
			"previous")
		},
		getNextModel: function(e, d) {
			return this._getSiblingNode(e, {
				siblingType: "model",
				siblingName: d
			},
			"next")
		},
		getPreviousEvent: function(e, d) {
			return this._getSiblingNode(e, {
				siblingType: "event",
				siblingName: d
			},
			"previous")
		},
		getNextEvent: function(e, d) {
			return this._getSiblingNode(e, {
				siblingType: "event",
				siblingName: d
			},
			"next")
		},
		_getSiblingNode: function(f, d, g) {
			var e;
			if (!f) {
				return null
			}
			e = f[[g, "Sibling"].join("")];
			return (e && (d.siblingName === e[[d.siblingType, "Name"].join("")])) ? e: this._getSiblingNode(e, d, g)
		},
		addListener: function(g, d) {
			if ("object" == typeof d) {
				var f;
				for (f in d) {
					switch (f) {
					case "load":
						g[f] = d[f];
						this._onload.queue.push(g);
						break;
					case "callback":
						g[f] = d[f];
						break;
					case "mouseenter":
					case "mouseleave":
						if (a.addEventListener) {
							var e = {
								mouseenter: "mouseover",
								mouseleave: "mouseout"
							};
							g["on" + e[f]] = (function(i, h) {
								return function(k) {
									var j = k.relatedTarget;
									while (j && j != this) {
										try {
											j = j.parentNode
										} catch(k) {
											break
										}
									}
									if (j != this) {
										g[i] = h;
										g[i]()
									}
								}
							})(f, d[f])
						} else {
							g["on" + f] = d[f]
						}
						break;
					default:
						g["on" + f] = d[f]
					}
				}
			}
		},
		removeListener: function(d) {
			d.onclick = d.onfocus = d.onblur = d.onmouseout = d.onmouseover = d.onmouseenter = d.onmouserleave = d.onchange = null;
			return this
		},
		_onload: {
			execute: function() {
				var d = this.queue.length,
				e;
				for (e = 0; e < d; e++) {
					this.queue[e]["load"]();
					this.queue[e]["load"] = undefined
				}
				this.queue = []
			},
			queue: []
		},
		models: {
			fns: {}
		},
		events: {
			fns: {}
		},
		getHead: function() {
			this.head || (this.head = a.getElementsByTagName("head")[0]);
			return this.head
		},
		getBody: function() {
			this.body || (this.body = a.getElementsByTagName("body")[0]);
			return this.body
		}
	};
	b.getCSS = (function() {
		var d = [];
		return function(f) {
			var h = b.nodes.getHead(),
			e = 0,
			j,
			g = d.length - 1;
			for (; g >= 0; g--) {
				e = (d[g] == f) ? 1 : 0
			}
			if (e == 0) {
				j = a.createElement("link");
				j.setAttribute("rel", "stylesheet");
				j.setAttribute("type", "text/css");
				j.setAttribute("href", f);
				h.appendChild(j);
				d.push(f)
			}
		}
	})();
	b.getJS = (function() {
		var d = [];
		return function(g, k) {
			var j, f, l, e = 0,
			h = d.length - 1;
			for (; h >= 0; h--) {
				e = (d[h] == g) ? 1 : 0
			}
			if (e == 0) {
				d.push(g);
				j = b.nodes.getHead();
				f = a.createElement("script");
				f.setAttribute("src", g);
				if ("function" == typeof k) {
					f.onload = f.onreadystatechange = function() {
						if (!this.readyState || "loaded" == this.readyState || "complete" == this.readyState) {
							this.onload = this.onreadystatechange = null;
							k();
							k = undefined;
							f = undefined
						}
					}
				}
				j.appendChild(f)
			} else {
				if ("function" == typeof k) {
					k();
					k = undefined
				}
			}
		}
	})();
	b.ready = function(d) {
		if ("function" !== typeof d) {
			return
		}
		if (a.addEventListener) {
			a.addEventListener("DOMContentLoaded", d, false)
		} else {
			if (a.attachEvent) {
				a.attachEvent("onreadystatechange", d)
			}
		}
	};
	c.M = b;
	M.ready(function() {
		M(a)
	})
})(window);

(function(a) {
	a.fn.hoverDelay = function(c) {
		var g = {
			hoverDuring: 200,
			outDuring: 200,
			hoverEvent: function() {
				a.noop()
			},
			outEvent: function() {
				a.noop()
			}
		};
		var e = a.extend(g, c || {});
		var b, d, f = this;
		return a(this).each(function() {
			a(this).hover(function() {
				clearTimeout(d);
				b = setTimeout(function() {
					e.hoverEvent.apply(f)
				},
				e.hoverDuring)
			},
			function() {
				clearTimeout(b);
				d = setTimeout(function() {
					e.outEvent.apply(f)
				},
				e.outDuring)
			})
		})
	}
})(jQuery); (function() {
	M.addModelFns({
		string_text: {
			load: function() {
				var a = M.getModelArgs(this);
				if (a.value == $(this).attr("placeholder")) {
					a.value = ""
				}
				var e = this;
				var b = new core.stringDb(e, a.inputname, a.value);
				b.init();
				var c = e.childEvents.stringInput[0];
				var d = e.childEvents.stringAdd[0];
				$(d).click(function() {
					b.add($(c).val());
					$(c).val("");
					return false
				});
				$(c).bind("keypress",
				function(g) {
					var f = g.which || g.keyCode;
					if (f == 13) {
						b.add($(c).val());
						$(c).val("");
						return false
					}
					return true
				});
				$(c).bind("blur",
				function() {
					b.add($(c).val());
					$(c).val("");
					return false
				})
			}
		}
	})
})();
M.addModelFns({
	cate_tree: {
		load: function() {
			var mArgs = M.getModelArgs(this);
			var cate_taggle = this.childEvents.cate_taggle;
			var cate_move = this.childEvents.cate_move;
			var cate_add = this.childEvents.cate_add;
			var cate_edit = this.childEvents.cate_edit;
			var cate_trans = this.childEvents.cate_trans;
			var cate_del = this.childEvents.cate_del;
			var cate_user = this.childEvents.cate_user;
			$(cate_taggle).click(function() {
				var args = M.getEventArgs(this);
				if ($(this).hasClass("ico-show")) {
					$(this).removeClass("ico-show");
					$(this).addClass("ico-hide");
					$("#sub_" + args.id).show()
				} else {
					$(this).removeClass("ico-hide");
					$(this).addClass("ico-show");
					$("#sub_" + args.id).hide()
				}
			});
			core.plugFunc(mArgs.m,
			function() {
				eval("var obj = core." + mArgs.m + ";");
				obj.setArgs(mArgs);
				$(cate_add).click(function() {
					var args = M.getEventArgs(this);
					obj.addBox(args.id)
				});
				$(cate_edit).click(function() {
					var args = M.getEventArgs(this);
					obj.editBox(args.id)
				});
				$(cate_trans).click(function() {
					var args = M.getEventArgs(this);
					obj.transBox(args.id, this)
				});
				$(cate_del).click(function() {
					var args = M.getEventArgs(this);
					obj.delBox(args.id)
				});
				$(cate_move).click(function() {
					var args = M.getEventArgs(this);
					obj.moveSort(args.id, args.v)
				});
				$(cate_user).click(function() {
					var args = M.getEventArgs(this);
					obj.loadAddUser(args.id)
				})
			})
		}
	},
	account_save: {
		callback: function(a) {
			ui.success(a.info);
			setTimeout(function() {
				location.href = location.href
			},
			1500)
		}
	},
	invite_colleague_form: {
		callback: function(a) {
			ui.success(a.info);
			ui.box.close()
		}
	},
	drop_menu_list: {
		load: function() {
			var a = this.parentNode,
			b = this;
			M.addListener(a, {
				mouseenter: function() {
					var c = this.className;
					this.className = [c, " drop"].join("");
					b.style.display = "block"
				},
				mouseleave: function() {
					var c = this.className;
					this.className = c.replace(/(\s+drop)+(\s+|$)/g, "");
					b.style.display = "none"
				}
			})
		}
	},
	widget_setform: {
		callback: function(a) {
			core.widget.afterSet(a)
		}
	},
	diy_widget: {
		load: function(b) {
			var d = this.childModels.widget_box;
			var c = this;
			var a = M.getModelArgs(this);
			core.plugFunc("widget",
			function() {
				core.loadFile(THEME_URL + "/js/ui.sortable.js",
				function() {
					$(c).sortable({
						items: ".ui-state-disabled",
						placeholder: "ui-selected",
						revert: 0.01,
						helper: "clone",
						update: function() {
							core.widget.dosort(a, c)
						}
					});
					$(d).disableSelection()
				})
			})
		}
	},
	big_img: {
		load: function() {
			$(this).find("img").each(function() {
				$(this).click(function() {
					ui.box.showImage($(this).attr("src"), $(this).attr("alt"))
				})
			})
		}
	},
	recent_view: {
		load: function() {
			if ("undefined" == typeof(this.isloading)) {
				this.isloading = true;
				var a = M.getModelArgs(this);
				a.is_widget = a.app_name;
				var b = this;
				$.post(U("widget/RecentView/ajaxshow"), a,
				function(c) {
					$(b).html(c);
					M(b)
				})
			}
		}
	},
	search_link: {
		load: function() {
			var c = this;
			var a = M.getModelArgs(c);
			var b = new CoreSearch(c, a);
			b.init();
			c.searchObj = b
		}
	},
	login_index: {
		callback: function(a) {
			if (a.info == "success") {
				location.href = a.data
			} else {
				$(".login-error").show();
				$(".login-error").html('<i class="icon-l-error"></i>' + a.info)
			}
		}
	},
	drop_div: {
		click: function() {
			if ($(this).hasClass("open")) {
				$(this).removeClass("open")
			} else {
				$(this).addClass("open")
			}
		}
	},
	highcharts_zx: {
		load: function() {
			var b = this;
			var a = M.getModelArgs(this);
			core.loadFile(THEME_URL + "/js/highcharts/highcharts.js",
			function() {
				core.loadFile(THEME_URL + "/js/highcharts/exporting.js",
				function() {
					$(b).highcharts({
						credits: {
							enabled: false
						},
						title: {
							text: a.title,
							x: -20
						},
						subtitle: {
							text: "",
							x: -20
						},
						xAxis: {
							categories: highcharts_ticks[a.data_id]
						},
						yAxis: {
							title: {
								text: ""
							},
							min: 0
						},
						tooltip: {
							valueSuffix: a.unit
						},
						legend: {
							layout: "vertical",
							align: "right",
							verticalAlign: "middle",
							borderWidth: 0
						},
						series: highchart_series[a.data_id]
					})
				})
			})
		}
	},
	highcharts_pie: {
		load: function() {
			var b = this;
			var a = M.getModelArgs(this);
			core.loadFile(THEME_URL + "/js/highcharts/highcharts.js",
			function() {
				core.loadFile(THEME_URL + "/js/highcharts/exporting.js",
				function() {
					$(b).highcharts({
						credits: {
							enabled: false
						},
						chart: {
							plotBackgroundColor: null,
							plotBorderWidth: null,
							plotShadow: false,
							width: a.width,
							height: a.height
						},
						title: {
							text: ""
						},
						tooltip: {
							pointFormat: "{series.name}:<b>{point.percentage:.1f}%</b>"
						},
						plotOptions: {
							pie: {
								allowPointSelect: true,
								cursor: "pointer",
								dataLabels: {
									enabled: a.datalabel,
									color: "#000000",
									connectorColor: "#000000",
									format: "<b>{point.name}</b>: {point.percentage:.1f} %"
								},
								size: a.size
							}
						},
						series: [{
							type: "pie",
							name: " ",
							data: highchart_series[a.data_id]
						}]
					})
				})
			})
		}
	},
	invate_user_box: {
		callback: function(a) {
			ui.success(a.info);
			ui.box.close()
		}
	}
});
M.addEventFns({
	quick_copy_link: {
		load: function() {
			var a = M.getEventArgs(this);
			var b = a.text;
			core.loadFile(THEME_URL + "/js/ZeroClipboard/ZeroClipboard.js?v=" + VERSION,
			function() {
				var c = new ZeroClipboard.Client();
				c.setHandCursor(true);
				c.addEventListener("mouseover",
				function(d) {
					c.setText(b)
				});
				c.addEventListener("complete",
				function(d, e) {
					ui.success(L("PUBLIC_HAS_COPY"), 2)
				});
				c.addEventListener("load",
				function(d) {
					debugstr("Flash movie loaded and ready.")
				});
				c.glue(a.id, a.id);
				$(window).resize(function() {
					c.reposition()
				})
			})
		}
	},
	send_invite_email: {
		click: function() {
			var a = this.parentModel.childEvents.invate_email[0].value;
			$.post(U("widget/Invite/sendEmail"), {
				email: a
			},
			function(b) {
				if (b.status == false) {
					ui.error(b.info)
				} else {
					ui.success(b.info)
				}
			},
			"json")
		}
	},
	invite_box: {
		click: function() {
			ui.box.load(U("widget/Invite/box", "", 0), L("PUBLIC_INVITE_COLLEAGUE"))
		}
	},
	invite_ico_add: {
		click: function() {
			var b = $(this).parent().find("input").val();
			var a = "<dd><input type='text' class='q-txt' name='invate_email[]' placeholder='" + L("PUBLIC_PLEASE_EMAIL_SNET") + "' value='" + b + "'><a href='javascript:;' event-node='del_invite_ico' class='del-opt'><i class='aicon-minus font14'></i></a></dd>";
			$(a).insertBefore($(this).parent());
			$(this).parent().find("input").val("");
			M(document.getElementById("invite_colleague_box"))
		}
	},
	del_invite_ico: {
		click: function() {
			$(this).parent().remove()
		}
	},
	fast_left: {
		load: function() {
			var b = M.getEventArgs(this);
			if (b.fast != 1) {
				return false
			}
			var a = $("#col1").width();
			var c = this;
			$(this).hoverDelay({
				hoverEvent: function() {
					var d = "layer-" + b.app_name;
					var g = $(this).offset();
					var f = function() {
						var h = $("#" + d).height();
						$(".layer_fast").hide();
						$("#" + d).show()
					};
					if ($("#" + d).length > 0 && $("#" + d).find("ul").length > 0) {
						f()
					} else {
						var e = '<div id="' + d + '" style="display:none" rel="' + b.app_name + '" class="layer_fast"><i class="icon-arrow-qlink"></i></div>';
						$(c).append(e);
						$.get(U(b.app_name + "/Index/showfast"), {
							uid: $config.mid
						},
						function(h) {
							if (h.status == 0) {
								return false
							}
							if (h.data) {
								$("#" + d).append(h.data);
								f()
							} else {
								$("#" + d).hover(function() {
									$("#" + d).hide()
								});
								$("#" + d).mouseover(function() {
									$("#" + d).hide()
								})
							}
						},
						"json")
					}
				},
				outEvent: function() {
					var d = "layer-" + b.app_name;
					$("#" + d).hide()
				}
			})
		}
	},
	login_index: {
		click: function() {
			if (getLength(this.parentModel.login_name.value) < 1) {
				$(".login-error").show();
				$(".login-error").html('<i class="icon-l-error"></i>' + L("PUBLIC_LOGIN_EMAIL_EMPTY"));
				return false
			}
			if (getLength(this.parentModel.login_password.value) < 1) {
				$(".login-error").show();
				$(".login-error").html('<i class="icon-l-error"></i>' + L("PUBLIC_LOGIN_PASSWORD_EMPTY"));
				return false
			}
			ajaxSubmit(this.parentModel)
		},
		load: function() {
			var a = this;
			$(this.parentModel).bind("keyup",
			function(b) {
				var b = b || window.event;
				if (b.keyCode == 13) {
					a.click()
				}
			});
			$(".dd-input .dd-txt").focus(function() {
				$(this).parent().parent().addClass("dd-focus")
			});
			$(".dd-input .dd-txt").blur(function() {
				$(this).parent().parent().removeClass("dd-focus")
			})
		}
	},
	loginbox: {
		click: function() {
			ui.box.load(U("www/Register/loginBox", "", 0), L("PUBLIC_PLEASE_LOGIN"))
		}
	},
	showCategory: {
		click: function() {
			var a = M.getEventArgs(this);
			var b = this;
			a.id = "undefined" == typeof($("#" + a.inputname).val()) ? a.id: $("#" + a.inputname).val();
			core.plugFunc("category",
			function() {
				core.category.loadSelect(b, a.model_name, a.app_name, a.method, a.id, a.inputname, a.callback)
			})
		}
	},
	widget_toggle: {
		click: function() {
			var a = M.getModelArgs(this.parentModel);
			var b = 1;
			if ($(this).hasClass("ico-circle-arrow-up")) {
				$(this).removeClass("ico-circle-arrow-up");
				$(this).addClass("ico-circle-arrow-down");
				b = 0
			} else {
				$(this).removeClass("ico-circle-arrow-down");
				$(this).addClass("ico-circle-arrow-up")
			}
			$(this.parentModel.childModels.widget_child[0]).toggle("500");
			core.widget.flodWidget(this, a, this.parentModel, b)
		}
	},
	widget_setup: {
		click: function() {
			$(this.parentModel.childModels.widget_setbox[0]).toggle("500")
		}
	},
	widget_cancel_set: {
		click: function() {
			$(this.parentModel).hide("500")
		}
	},
	widget_close: {
		click: function() {
			var a = M.getModelArgs(this.parentModel);
			core.widget.removeWidget(this, a, this.parentModel)
		}
	},
	widget_add: {
		click: function() {
			var a = M.getEventArgs(this);
			core.widget.addWidget(a)
		}
	},
	image_block: {
		click: function() {
			core.insertImage.show(this)
		},
		load: function() {
			core.plugFunc("insertImage")
		}
	},
	invite_colleague: {
		click: function() {
			ui.box.load(this.href, L("PUBLIC_INVITE_COLLEAGUE"));
			return false
		}
	},
	invite_addemail: {
		click: function() {
			var c = document.getElementById("email_input").value,
			a = $("#email_input"),
			d = this.parentModel.childEvents.email[0],
			b = d.cloneNode(true);
			b.value = "";
			a.append(b);
			M(b);
			return false
		}
	},
	face_card: {
		mouseenter: function() {
			var a = $(this).attr("uid");
			if ($config.mid < 1 || ($config.mid == a) || ($config.uid == a) || a < 1) {
				return false
			}
			core.facecard.init();
			core.facecard.show($(this), a)
		},
		mouseleave: function() {
			var a = $(this).attr("uid");
			if ($config.mid < 1 || ($config.mid == a) || ($config.uid == a) || a < 1) {
				return false
			}
			core.facecard.hide()
		},
		blur: function() {
			core.facecard.hide()
		}
	},
	more_person_info: {
		click: function() {
			var a;
			a = this.parentNode;
			a.style.display = "block";
			if ($(this).attr("rel") == "hide") {
				var b = "block";
				$(this).attr("rel", "show");
				$(".mod-person .person-info a").text(L("PUBLIC_PUT") + "↑")
			} else {
				var b = "none";
				$(this).attr("rel", "hide");
				$(".mod-person .person-info a").text(L("PUBLIC_OPEN_MORE") + "↓")
			}
			while (a = a.nextSibling) { ("LI" === a.tagName) && (a.style.display = b)
			}
			return false
		}
	},
	ico_wallet: {
		mouseenter: function() {
			this._model.style.display = "block"
		},
		mouseleave: function() {
			this._model.style.display = "none"
		},
		load: function() {
			var a = M.getModels("layer_wallet");
			this._model = a[0]
		}
	},
	ico_level: {
		mouseenter: function() {
			this._model.style.display = "block"
		},
		mouseleave: function() {
			this._model.style.display = "none"
		},
		load: function() {
			var a = M.getModels("layer_level");
			this._model = a[0]
		}
	},
	share: {
		click: function() {
			share(M.getEventArgs(this))
		}
	},
	setremark: {
		click: function() {
			var b = $(this).attr("remark");
			var a = $(this).attr("uid");
			ui.box.load(U("widget/Remark/edit", {
				remark: b,
				uid: a
			},
			0), L("PUBLIC_EDIT_FOLLWING"))
		}
	},
	doFollow: {
		click: function() {
			follow.doFollow(this);
			return false
		},
		load: function() {
			follow.createBtn(this)
		}
	},
	unFollow: {
		click: function() {
			follow.unFollow(this);
			return false
		},
		load: function() {
			follow.createBtn(this)
		}
	},
	setFollowGroup: {
		click: function() {
			var a = M.getEventArgs(this);
			follow.setFollowGroup(this, a.fid)
		}
	},
	follow_check: {
		click: function(b) {
			var a = this.getElementsByTagName("input")[0];
			setTimeout(function() {
				a.checked = !a.checked;
				a = undefined
			},
			1);
			return false
		}
	},
	comment: {
		click: function() {
			var a = M.getEventArgs(this);
			var b = this.parentModel.childModels.comment_detail[0];
			core.comment.init(a, b);
			core.comment.display();
			return false
		}
	},
	comment_more: {
		click: function() {
			var a = M.getEventArgs(this);
			var b = document.getElementById("comment_lists" + a.row_id);
			core.comment.loadMore(this, a, b)
		}
	},
	reply_comment: {
		click: function() {
			var a = M.getEventArgs(this);
			var c = this.parentModel.parentModel;
			var b = c.childModels.comment_textarea[0].childEvents.do_comment[0];
			$(b).attr("to_comment_id", a.to_comment_id);
			$(b).attr("to_uid", a.to_uid);
			$(b).attr("to_comment_uname", a.to_comment_uname);
			core.comment.init(a, c);
			core.comment.initReply()
		}
	},
	comment_del: {
		click: function() {
			var b = M.getEventArgs(this);
			var c = this;
			var a = function() {
				core.comment.delComment(b.comment_id, b.row_id);
				$(c.parentModel).slideUp()
			};
			ui.confirm(this, L("PUBLIC_DELETE_THISNEWS"), a)
		}
	},
	do_comment: {
		click: function() {
			core.toggleSubmitCSS(this, "btn-green-mini", "btn-mini", 800);
			var a = M.getEventArgs(this);
			a.to_comment_id = $(this).attr("to_comment_id");
			a.to_uid = $(this).attr("to_uid");
			a.to_comment_uname = $(this).attr("to_comment_uname");
			a.addToEnd = $(this).attr("addtoend");
			var d = this.parentModel.parentModel;
			core.comment.init(a, d);
			var c = this;
			var b = function() {
				$(c).attr("to_uid", "0");
				$(c).attr("to_comment_id", "0");
				$(c).attr("to_comment_uname", "");
				if (a.closeBox == 1) {
					ui.box.close();
					ui.success(L("PUBLIC_CENTSUCCESS"))
				}
			};
			core.comment.addComment(b)
		},
		load: function() {
			var a = M.getEventArgs(this);
			a.to_comment_id = $(this).attr("to_comment_id");
			a.to_uid = $(this).attr("to_uid");
			a.to_comment_uname = $(this).attr("to_comment_uname");
			a.addToEnd = $(this).attr("addtoend");
			var b = this.parentModel.parentModel;
			core.comment.init(a, b)
		}
	},
	insert_face: {
		click: function() {
			var a = this.parentModel.childEvents.mini_editor_textarea[0];
			core.face.init(this, a)
		},
		load: function() {
			core.plugFunc("face")
		}
	},
	insert_topic: {
		click: function() {
			var g = L("PUBLIC_TEXTARE_PLEASE");
			var e = new RegExp(g, "g");
			var d = $(this.parentModel.childEvents.mini_editor_textarea[0]);
			var a;
			if (d.val().search(e) == "-1") {
				d.insertAtCaret(g);
				var f = this.parentModel.childEvents.mini_editor_textarea[0];
				a = e.exec(d.val());
				var c = e.lastIndex - 1;
				var h = e.lastIndex - g.length + 1;
				if (document.selection) {
					var b = f.createTextRange();
					b.collapse(true);
					b.moveEnd("character", c);
					b.moveStart("character", h);
					b.select()
				} else {
					if (f.selectionStart || (f.selectionStart == "0")) {
						f.selectionStart = h;
						f.selectionEnd = c
					}
				}
				f.click();
				return
			} else {
				ui.error(L("PUBLIC_TOPIC_ADDED"))
			}
		}
	},
	insert_at: {
		click: function() {
			var a = this.parentModel.childEvents.mini_editor_textarea[0];
			$(a).insertAtCaret("@");
			a.click()
		}
	},
	insert_video: {
		click: function() {
			var a = this.parentModel.childEvents.mini_editor_textarea[0];
			core.insertVideo.init(a);
			core.insertVideo.show(this)
		},
		load: function() {
			core.plugFunc("insertVideo")
		}
	},
	weibo_insert_vote: {
		click: function() {
			this.init();
			core.vote.addVote()
		},
		load: function() {
			core.plugFunc("vote");
			var b = this;
			var a = M.getEventArgs(this);
			this.init = function() {
				b.vote = core.vote;
				b.vote.init(b, a)
			}
		}
	},
	feed_show_vote: {
		click: function() {
			var a = M.getEventArgs(this);
			var b = a.vote_id;
			var a = M.getEventArgs(this);
			$(this.parentModel).find("div").each(function() {
				if ($(this).attr("rel") == "small") {
					$(this).hide()
				} else {
					if ($(this).attr("rel") == "big") {
						obj = $(this);
						obj.show();
						if (obj.find(".feed-vote-show").length == 0) {
							var c = U("widget/Vote/ajaxShow");
							$.post(c, {
								vote_id: b,
								tpl: "default"
							},
							function(d) {
								obj.append(d.data);
								M(obj[0])
							},
							"json")
						}
					}
				}
			})
		},
		load: function() {
			core.plugFunc("vote");
			var b = this;
			var a = M.getEventArgs(this);
			this.init = function() {
				b.vote = core.vote;
				b.vote.init(b, a)
			}
		}
	},
	feed_hide_vote: {
		click: function() {
			$(this.parentModel).find("div").each(function() {
				if ($(this).attr("rel") == "big") {
					$(this).hide()
				} else {
					if ($(this).attr("rel") == "small") {
						$(this).show()
					}
				}
			})
		}
	},
	mini_editor_textarea: {
		click: function() {
			var b = this;
			var a = function() {
				if ("undefined" != typeof(b.parentModel.childModels.nums_left)) {
					core.checkNums(b, b.parentModel.childModels.nums_left[0], $config.initNums, 0)
				}
				var c = M.getModelArgs(b.parentModel);
				if ("undefined" != typeof(c.post_event) && $(b.parentModel.childEvents[c.post_event]).length > 0) {
					var d = core.getLeftNums(b, $config.initNums, true);
					if (d > 0 && d < $config.initNums) {
						$(b.parentModel.childEvents[c.post_event][0]).removeClass("btn-gray");
						$(b.parentModel.childEvents[c.post_event][0]).addClass("btn-green")
					} else {
						$(b.parentModel.childEvents[c.post_event][0]).addClass("btn-gray");
						$(b.parentModel.childEvents[c.post_event][0]).removeClass("btn-green")
					}
				}
				return true
			};
			this.t = setInterval(a, 250)
		},
		blur: function() {
			clearInterval(this.t);
			this.t = null
		},
		load: function() {
			var b = this;
			var a = M.getEventArgs(this);
			var c = "undefined" == typeof(a.has_out_user) ? 0 : a.has_out_user;
			setAutoTips({
				elem: b,
				url: U("widget/Search/searchUser", {
					follow: 0,
					limit: 10,
					group: 1,
					has_out_user: c
				}),
				mod: "user"
			});
			setAutoTips({
				elem: b,
				url: U("widget/Topic/search"),
				mod: "topic"
			});
			var a = M.getModelArgs(b.parentModel);
			if (getLength($(this).html()) > 0 && $(b.parentModel.childEvents[a.post_event]).length > 0) {
				$(b.parentModel.childEvents[a.post_event][0]).addClass("btn-green")
			}
			$(this).click();
			$(this).keydown(function(d) {
				var d = d ? d: window.event;
				if (d.ctrlKey && d.keyCode == 13) {
					d.keyCode = 0;
					d.returnValue = false;
					$(b.parentModel.childEvents[a.post_event][0]).click()
				}
			})
		}
	},
	post_feed: {
		click: function() {
			if ($(".upload_tips").length > 0) {
				ui.error(L("PUBLIC_ATTACH_UPLOADING_NOSENT"));
				return false
			}
			var a = this.parentModel.childEvents.mini_editor_textarea[0];
			if ("undefined" != typeof(this.parentModel.childModels.nums_left)) {
				var b = $(this.parentModel.childModels.nums_left[0]).find("span").html();
				if (b == $config.initNums && $(this.parentModel).find(".input-content").length < 1) {
					flashTextarea(a);
					return false
				}
				if (!core.checkNums(a, this.parentModel.childModels.nums_left[0], $config.initNums, 0)) {
					if ($(this.parentModel).find(".input-content").length > 0) {
						if ($(this.parentModel).find(".input-content").attr("uploadcontent") == "image") {
							a.value = L("PUBLIC_SHARE_IMAGES")
						} else {
							a.value = L("PUBLIC_SHARE_FILES")
						}
					} else {
						flashTextarea(a);
						return false
					}
				}
			}
			core.weibo.post_feed(this, a)
		},
		load: function() {
			if ("undefined" == typeof(core.weibo)) {
				core.plugFunc("weibo")
			}
		}
	},
	post_share: {
		click: function() {
			var a = this.parentModel.childEvents.mini_editor_textarea[0];
			if ("undefined" != typeof(this.parentModel.childModels.nums_left)) {
				if (!core.checkNums(a, this.parentModel.childModels.nums_left[0], $config.initNums, 0)) {
					flashTextarea(a);
					return false
				}
			}
			core.share.post_share(this, a)
		},
		load: function() {
			core.plugInit("share")
		}
	},
	img_small: {
		click: function() {
			$(this.parentModel).find("div").each(function() {
				if ($(this).attr("rel") == "small") {
					$(this).hide()
				} else {
					if ($(this).attr("rel") == "big") {
						$(this).show()
					}
				}
			});
			$(this.parentModel).find(".big li").each(function() {
				$(this).css("width", $(this).find(".imgsmall").width())
			})
		}
	},
	img_big: {
		click: function() {
			$(this.parentModel).find("div").each(function() {
				if ($(this).attr("rel") == "small") {
					$(this).show()
				} else {
					if ($(this).attr("rel") == "big") {
						$(this).hide()
					}
				}
			})
		}
	},
	video_small: {
		click: function() {
			var a = M.getEventArgs(this);
			$(this.parentModel).find("div").each(function() {
				if ($(this).attr("rel") == "small") {
					$(this).hide()
				} else {
					if ($(this).attr("rel") == "big") {
						$(this).find("li").html(core.insertVideo.showflash(a.host, a.flashvar));
						$(this).show()
					}
				}
			})
		},
		load: function() {
			core.plugFunc("insertVideo")
		}
	},
	video_big: {
		click: function() {
			$(this.parentModel).find("div").each(function() {
				if ($(this).attr("rel") == "small") {
					$(this).show()
				} else {
					if ($(this).attr("rel") == "big") {
						$(this).hide();
						$(this).find("li").html("")
					}
				}
			})
		}
	},
	inviteToAction: {
		click: function() {
			var j = M.getEventArgs(this);
			var b = j.sid;
			var c = j.stable;
			var d = j.initHTML;
			var h = j.curtable;
			var i = j.curid;
			var f = j.app_name;
			var g = j.cancomment;
			var k = j.add;
			var e = j.action_info ? j.action_info: "ANSWER";
			if ($config.mid < 1) {
				return false
			}
			if ("undefined" == typeof(g)) {
				g = 0
			}
			var a = U("widget/Share/inviteToAction") + "&sid=" + b + "&stable=" + c + "&curid=" + i + "&curtable=" + h + "&app_name=" + f + "&initHTML=" + d + "&cancomment=" + g + "&add=" + k + "&action_info=" + e;
			if ($("#uibox").length > 0) {
				return false
			}
			ui.box.load(a, L("PUBLIC_INVITE_" + e));
			return false
		}
	},
	feed_show_detail: {
		click: function() {
			var d = M.getEventArgs(this);
			var c = $(this).attr("alt");
			var a, b;
			$(this.parentModel).find(".share-detail").each(function() {
				if ($(this).attr("rel") == "small") {
					a = $(this)
				}
				if ($(this).attr("rel") == "big") {
					b = $(this)
				}
			});
			if (b.css("display") != "none") {
				b.slideUp()
			} else {
				if (b.children().length < 2) {
					$.post(c, {},
					function(e) {
						b.append(e);
						b.slideDown();
						if (d.hide_self == 1) {
							a.slideUp()
						}
						M(b[0])
					})
				} else {
					b.slideDown();
					if (d.hide_self == 1) {
						a.slideUp()
					}
				}
			}
		}
	},
	feed_hide_detail: {
		click: function() {
			$(this.parentModel).find(".share-detail").each(function() {
				if ($(this).attr("rel") == "big") {
					$(this).slideUp()
				}
				if ($(this).attr("rel") == "small") {
					$(this).slideDown()
				}
			})
		}
	},
	default_space: {
		click: function() {
			var a = M.getEventArgs(this);
			var c = this.parentModel;
			var b = this;
			if ($(b).hasClass("ico-choosed")) {
				return false
			}
			core.SAjax(U("core/Do/setDefault"), {
				space_name: a.space_name
			},
			function(d) {
				if (d.status == 1) {
					setTimeout(function() {
						location.href = location.href
					},
					1500)
				} else {
					ui.error(d.info)
				}
			},
			"json")
		},
		load: function() {
			var a = this;
			$(this).mouseover(function() {
				$(a).parent().find(".qg-tips").show()
			});
			$(this).mouseout(function() {
				$(a).parent().find(".qg-tips").hide()
			})
		}
	},
	feed_show_document: {
		click: function() {
			var a = M.getEventArgs(this);
			$.get(U("widget/ShowDocument/renderJSON"), {
				is_widget: "core",
				attach_id: a.attach_id,
				width: "800px"
			},
			function(b) {
				ui.box.showDoc(b)
			},
			"json")
		}
	},
	reader: {
		click: function() {
			var a = $("#reader_title").html() + $("#reader").html();
			ui.box.showReader(a)
		}
	}
});
var share = function(b) {
	if ($config.mid < 1) {
		return false
	}
	if ("undefined" == typeof(b.cancomment)) {
		b.cancomment = 0
	}
	var a = U("widget/Share/box", b);
	if ($("#uibox").length > 0) {
		return false
	}
	ui.box.load(a, L("PUBLIC_SHARE"));
	return false
};
var follow = {
	btnClass: {
		doFollow: "btn btn-green",
		unFollow: "btn btn-red",
		haveFollow: "btn btn-gray",
		eachFollow: "btn btn-gray"
	},
	flagClass: {
		doFollow: "aicon-plus",
		unFollow: "aicon-minus",
		haveFollow: "ico-already",
		eachFollow: "aicon-mutual"
	},
	btnText: {
		doFollow: L("PUBLIC_ADD_FOLLOWING"),
		unFollow: L("PUBLIC_ERROR_FOLLOWING"),
		haveFollow: L("PUBLIC_FOLLOWING"),
		eachFollow: L("PUBLIC_DUBLE_FOLLOWING")
	},
	createBtn: function(d, a) {
		var b = M.getEventArgs(d),
		a = (0 == b.following) ? "doFollow": ((0 == b.follower) ? "haveFollow": "eachFollow"),
		f = this.btnClass[a],
		g = this.flagClass[a],
		c = this.btnText[a],
		e = ['<span><b class="', g, '"></b>', c, "</span>"].join("");
		d.innerHTML = e;
		d.className = f;
		switch (a) {
		case "haveFollow":
		case "eachFollow":
			M.addListener(d, {
				mouseenter: function() {
					var h = this.getElementsByTagName("b")[0],
					i = h.nextSibling;
					this.className = follow.btnClass.unFollow;
					h.className = follow.flagClass.unFollow;
					i.nodeValue = follow.btnText.unFollow
				},
				mouseleave: function() {
					var h = this.getElementsByTagName("b")[0],
					i = h.nextSibling;
					this.className = f;
					h.className = g;
					i.nodeValue = c
				}
			});
			break;
		default:
		}
	},
	doFollow: function(c) {
		var d = this,
		b = M.getEventArgs(c),
		a = c.getAttribute("href") || [U("core/Follow/doFollow"), "&fid=", b.uid].join("");
		$.get(a, {},
		function(e) {
			if (1 == e.status) {
				core.facecard.deleteUser(b.uid);
				c.setAttribute("event-node", "unFollow");
				c.setAttribute("href", [U("core/Follow/unFollow"), "&fid=", b.uid].join(""));
				M.setEventArgs(c, ["uid=", b.uid, "&uname=", b.uname, "&following=", e.data.following, "&follower=", e.data.follower].join(""));
				M.removeListener(c);
				M(c);
				d.updateFollowCount(1);
				followGroupSelectorBox(b.uid)
			} else {
				ui.error(e.info)
			}
		},
		"json")
	},
	setFollowGroup: function(b, c) {
		var a = U("core/FollowGroup/selectorBox") + "&fid=" + c;
		ui.box.load(a, L("PUBLIC_SET_GROUP"))
	},
	unFollow: function(b) {
		var c = this,
		a = M.getEventArgs(b);
		url = b.getAttribute("href") || [U("core/Follow/unFollow"), "&fid=", a.uid].join("");
		$.get(url, {},
		function(d) {
			if (1 == d.status) {
				ui.success(d.info);
				core.facecard.deleteUser(a.uid);
				if ("following_list" == a.refer) {
					var e = b.parentModel;
					e.parentNode.removeChild(e)
				} else {
					b.setAttribute("event-node", "doFollow");
					b.setAttribute("href", [U("core/Follow/doFollow"), "&fid=", a.uid].join(""));
					M.setEventArgs(b, ["uid=", a.uid, "&uname=", a.uname, "&following=", d.data.following, "&follower=", d.data.follower].join(""));
					M.removeListener(b);
					M(b)
				}
				c.updateFollowCount( - 1)
			} else {
				ui.error(d.info)
			}
		},
		"json")
	},
	updateFollowCount: function(c) {
		var b, a = M.getEvents("following_count");
		if (a) {
			b = a.length;
			while (b-->0) {
				a[b].innerHTML = parseInt(a[b].innerHTML) + c
			}
		}
	}
};
function followGroupSelectorList(b, c) {
	var a = b.offset();
	$.post(U("core/FollowGroup/selectorList"), {
		fid: c
	},
	function(d) {
		if ($("#followGroupList").length > 0) {
			if ($("#followGroupList").attr("rel") == c) {
				$("#followGroupList").remove()
			} else {
				$("#followGroupList").attr("rel", c);
				$("#followGroupList").html(d)
			}
		} else {
			$("body").append('<div id="followGroupList" rel="' + c + '" class="layer-follow-list" >' + d + "</div>")
		}
		$("#followGroupList").css({
			left: a.left + "px",
			top: a.top + b.height() + 14 + "px",
			display: "block"
		});
		$("#followGroupSelector").find("label").hover(function() {
			$(this).addClass("hover")
		},
		function() {
			$(this).removeClass("hover")
		})
	})
}
function addFollowGroup(b) {
	var a = b.offset();
	$.post(U("core/FollowGroup/selectorList"), {
		type: "add"
	},
	function(c) {
		$("body").append('<div id="followGroupList" rel="" class="layer-follow-list">' + c + "</div>");
		$("#followGroupList").css({
			left: a.left + "px",
			top: a.top + b.height() + 14 + "px",
			display: "block"
		});
		$("#followGroupSelector").find("label").hover(function() {
			$(this).addClass("hover")
		},
		function() {
			$(this).removeClass("hover")
		})
	})
}
function followGroupSelectorBox(a) {
	ui.box.load(U("core/FollowGroup/selectorBox") + "&fid=" + a, L("PUBLIC_FOLLOWING_SUCCESS"))
}
function followGroupSelectorClose(a) {
	$(".followGroupStatus" + a).hide();
	$(".followGroupStatus" + a).html("")
}
function setFollowGroupTab(a) {
	var b = a ? L("PUBLIC_EDIT_GROUP") : L("PUBLIC_CREATE_GROUP");
	a = a ? "&gid=" + a: "";
	ui.box.load(U("core/FollowGroup/setGroupTab") + a, b)
}
M.addEventFns({
	feed_tab_btn: {
		click: function() {
			if ($(this).hasClass("ico-circle-arrow-up")) {
				$(this).removeClass("ico-circle-arrow-up");
				$(this).addClass("ico-circle-arrow-down");
				$(".mod-feed-tab").show()
			} else {
				$(this).removeClass("ico-circle-arrow-down");
				$(this).addClass("ico-circle-arrow-up");
				$(this).attr("title", L("PUBLIC_PUT"));
				$(".mod-feed-tab").hide();
				$(this).attr("title", L("PUBLIC_OPEN"))
			}
		}
	},
	post_feed_box: {
		click: function() {
			var a = this.parentModel.childEvents.mini_editor_textarea[0];
			core.weibo.post_feed(this, a, true)
		},
		load: function() {
			if ("undefined" == typeof(core.weibo)) {
				core.plugFunc("weibo",
				function() {
					core.weibo.doInitUrl()
				})
			}
		}
	},
	delFeed: {
		click: function() {
			var a = M.getEventArgs(this);
			var c = this;
			var b = function() {
				$.post(U("widget/Feed/removeFeed"), {
					feed_id: a.feed_id,
					app_name: a.app_name,
					model_name: a.model_name,
					notice: a.notice
				},
				function(d) {
					if (d.status == 1) {
						if ($("#feed" + a.feed_id).length > 0) {
							$("#feed" + a.feed_id).slideUp()
						} else {
							$(c.parentModel).slideUp()
						}
						updateUserData("feed_count", -1, a.uid)
					} else {
						ui.error(L("PUBLIC_DELETE_ERROR"))
					}
				},
				"json")
			};
			ui.confirm(this, L("PUBLIC_DELETE_THISNEWS"), b)
		}
	},
	addFollowgroup: {
		click: function() {
			var a = function() {
				if ("undefined" != typeof(global_group)) {
					if (global_group.group_name != "") {
						var d = M.getModels("layer_group_list");
						var c = $(d[0]).find("li").size();
						var b = '<li><a href="javascript:void(0);">' + global_group.group_name + "</a></li>";
						$(b).insertBefore($(d[0]).find("li").eq(c - 3))
					}
				}
			};
			$.post(U("widget/FollowGroup/checkGroup"), {},
			function(b) {
				if (b.status == 0) {
					ui.error(b.data)
				} else {
					ui.box.load(U("widget/FollowGroup/addgroup", "", 0), L("PUBLIC_CREATE_GROUP"), a)
				}
			},
			"json")
		}
	},
	editFollowgroup: {
		click: function() {
			ui.box.load(U("widget/FollowGroup/editgroup", "", 0), L("PUBLIC_MANAGE_GROUP"))
		}
	}
});
M.addModelFns({
	myfollow: {
		click: function() {
			var a = $(this).offset();
			$(".layer-group-list").css({
				left: a.left + "px",
				top: a.top + $(this).height() + "px"
			}).show();
			$(this).addClass("open");
			$(".layer-group-list").attr("_mouse", "on")
		},
		mouseleave: function() {
			var b = this;
			var a = function() {
				if ($(".layer-group-list").attr("_mouse") != "on") {
					$(".layer-group-list").hide();
					$(b).removeClass("open")
				} else {
					$(b).addClass("open")
				}
			};
			$(".layer-group-list").attr("_mouse", "left");
			setTimeout(a, 200)
		}
	},
	layer_group_list: {
		mouseenter: function() {
			$(this).attr("_mouse", "on")
		},
		mouseleave: function() {
			$(this).attr("_mouse", "left");
			var a = function() {
				if ($(".layer-group-list").attr("_mouse") != "on") {
					$(".layer-group-list").hide();
					var b = M.getModels("myfollow");
					$(b[0]).removeClass("open")
				}
			};
			setTimeout(a, 200)
		}
	},
	audio_click: {
		click: function() {
			var c = this.parentModel;
			var b = M.getModelArgs(c);
			var e = swfobject.getObjectById("sound");
			var a = b.f.replace(".amr", ".mp3");
			if (e) {
				if ($(c).hasClass("audio-Playing")) {
					$(c).removeClass("audio-Playing");
					a = "/apps/core/static/js/sound/stop.mp3"
				} else {
					$(c).addClass("audio-Playing");
					var d = parseInt(b.t, 10) * 1000 + 1000;
					setTimeout(function() {
						$(c).removeClass("audio-Playing")
					},
					d)
				}
				e.SetVariable("f", a);
				e.GotoFrame(1)
			}
		}
	}
});
M.addEventFns({
	cancelNotice: {
		click: function() {
			var b = M.getEventArgs(this);
			var a = function() {
				$.post(U("core/Do/cancelNotice"), {},
				function(c) {
					if (c == 1) {
						$("#notice").slideUp()
					} else {
						ui.error(L("PUBLIC_ADMIN_OPRETING_ERROR"))
					}
				})
			};
			ui.confirm(this, L("PUBLIC_CAN_CANCEL_NOTICE"), a)
		}
	}
});

var ajaxSubmit = function(form) {
	var args = M.getModelArgs(form);
	if ("undefined" == typeof(form.ajaxSubmitIng)) {
		form.ajaxSubmitIng = 0
	} else {
		if (form.ajaxSubmitIng > 0) {
			return false
		}
	}
	form.ajaxSubmitIng += 1;
	M.getJS(THEME_URL + "/js/jquery.form.js?v=" + VERSION,
	function() {
		var options = {
			dataType: "json",
			success: function(txt) {
				form.ajaxSubmitIng = 0;
				if (1 == txt.status) {
					if ("function" === typeof form.callback) {
						form.callback(txt)
					} else {
						if ("string" == typeof(args.callback)) {
							eval(args.callback + "()")
						} else {
							ui.success(txt.info)
						}
					}
				} else {
					ui.error(txt.info, 1.5)
				}
				if ("undefined" != typeof args.ajaxbutton) {
					$(form.childEvents[args.ajaxbutton][0]).removeClass("btn-ing")
				}
			}
		};
		$(form).append('<input type="hidden" name="token" value="' + $config.token + '">');
		$(form).ajaxSubmit(options)
	})
}; (function() {
	M.addModelFns({
		normal_form: {
			submit: function() {
				var oCollection = this.elements,
				nL = oCollection.length,
				bValid = true,
				dFirstError;
				for (var i = 0; i < nL; i++) {
					var dInput = oCollection[i],
					sName = dInput.name;
					if (!sName || !dInput.getAttribute("event-node")) {
						continue
					} ("function" === typeof dInput.onblur) && dInput.onblur();
					if (!dInput.bIsValid) {
						bValid = false;
						dFirstError = dFirstError || dInput
					}
				}
				dFirstError && dFirstError.focus();
				return bValid
			}
		}
	}).addEventFns({
		input_text: {
			focus: function() {
				this.className = "q-txt-focus";
				return false
			},
			blur: function() {
				this.className = "q-txt";
				var oArgs = M.getEventArgs(this),
				min = oArgs.min ? parseInt(oArgs.min) : 0,
				max = oArgs.max ? parseInt(oArgs.max) : 0;
				if (min <= 0 && max <= 0) {
					return
				}
				var dTips = (this.parentModel.childEvents[this.getAttribute("name") + "_tips"] || [])[0],
				sValue = this.value;
				sValue = sValue.replace(/(^\s*)|(\s*$)/g, "");
				var nL = sValue.replace(/[^\x00-\xff]/ig, "xx").length / 2;
				if (nL <= min - 1 || (max && nL > max)) {
					dTips && (dTips.style.display = "none");
					tips.error(this, oArgs.error);
					this.bIsValid = false
				} else {
					var _this = this;
					if (oArgs.uname == 1) {} else {
						tips.success(this);
						dTips && (dTips.style.display = "");
						this.bIsValid = true
					}
				}
				return false
			},
			load: function() {
				this.className = "q-txt"
			}
		},
		input_nums: {
			focus: function() {
				this.className = "q-txt-focus";
				return false
			},
			blur: function() {
				this.className = "q-txt";
				var oArgs = M.getEventArgs(this),
				min = oArgs.min ? parseInt(oArgs.min) : 0,
				type = oArgs.type,
				max = oArgs.max ? parseInt(oArgs.max) : 0;
				if (min <= 0 && max <= 0) {
					return
				}
				var dTips = (this.parentModel.childEvents[this.getAttribute("name") + "_tips"] || [])[0],
				sValue = this.value;
				var re = /^[0-9]*$/;
				if (!re.test(sValue)) {
					dTips && (dTips.style.display = "none");
					tips.error(this, L("PUBLIC_TYPE_ISNOT"));
					this.bIsValid = false;
					return false
				}
				var pholen = sValue.length;
				if (type != "QQ") {
					if (pholen > 11) {
						tips.error(this, L("PUBLIC_NUMBER_ERROR"));
						this.bIsValid = false;
						return false
					}
				}
				sValue = sValue.replace(/(^\s*)|(\s*$)/g, "");
				var nL = sValue.replace(/[^\x00-\xff]/ig, "xx").length / 2;
				if (nL <= min - 1 || (max && nL > max)) {
					dTips && (dTips.style.display = "none");
					tips.error(this, oArgs.error);
					this.bIsValid = false
				} else {
					tips.success(this);
					dTips && (dTips.style.display = "");
					this.bIsValid = true
				}
				return false
			},
			load: function() {
				this.className = "q-txt"
			}
		},
		textarea: {
			focus: function() {
				this.className = "q-textarea-focus"
			},
			blur: function() {
				this.className = "q-textarea";
				var oArgs = M.getEventArgs(this),
				min = oArgs.min ? parseInt(oArgs.min) : 0,
				max = oArgs.max ? parseInt(oArgs.max) : 0;
				if (min <= 0 && max <= 0) {
					return
				}
				if ($.trim(this.value)) {
					tips.success(this);
					this.bIsValid = true
				} else {
					if ("undefined" != typeof(oArgs.error)) {
						tips.error(this, oArgs.error);
						this.bIsValid = false
					}
				}
			},
			load: function() {
				this.className = "q-textarea"
			}
		},
		input_date: {
			focus: function() {
				this.className = "q-txt-focus";
				var dDate = this,
				oArgs = M.getEventArgs(this);
				M.getJS(THEME_URL + "/js/rcalendar.js?v=" + VERSION,
				function() {
					rcalendar(dDate, oArgs.mode)
				})
			},
			blur: function() {
				this.className = "q-txt";
				var dTips = (this.parentModel.childEvents[this.getAttribute("name") + "_tips"] || [])[0],
				oArgs = M.getEventArgs(this);
				if (oArgs.min == 0) {
					return true
				}
				var _this = this;
				setTimeout(function() {
					sValue = _this.value;
					if (!sValue) {
						dTips && (dTips.style.display = "none");
						tips.error(_this, oArgs.error);
						this.bIsValid = false
					} else {
						tips.success(_this);
						dTips && (dTips.style.display = "");
						_this.bIsValid = true
					}
				},
				250)
			},
			load: function() {
				this.className = "q-txt"
			}
		},
		email: {
			focus: function() {
				this.className = "q-txt-focus";
				var x = $(this).offset();
				$(this.dTips).css({
					left: x.left + "px",
					top: x.top + $(this).height() + 12 + "px",
					width: $(this).width() + "px"
				})
			},
			blur: function() {
				this.className = "q-txt";
				var dEmail = this;
				var sUrl = dEmail.getAttribute("checkurl"),
				sValue = dEmail.value;
				if (!sUrl || (this.dSuggest && this.dSuggest.isEnter)) {
					return
				}
				$.post(sUrl, {
					email: sValue
				},
				function(sTxt) {
					oTxt = eval("(" + sTxt + ")");
					var oArgs = M.getEventArgs(dEmail);
					if (oTxt.status) {
						"false" == oArgs.success ? tips.clear(dEmail) : tips.success(dEmail);
						dEmail.bIsValid = true
					} else {
						"false" == oArgs.error ? tips.clear(dEmail) : tips.error(dEmail, oTxt.info);
						dEmail.bIsValid = false
					}
					return true
				});
				$(this.dTips).hide()
			},
			load: function() {
				this.className = "q-txt";
				var dEmail = this,
				oArgs = M.getEventArgs(this);
				if (!oArgs.suffix) {
					return false
				}
				var aSuffix = oArgs.suffix.split(","),
				dFrag = document.createDocumentFragment(),
				dTips = document.createElement("div"),
				dUl = document.createElement("ul");
				this.dTips = $(dTips);
				$("body").append(this.dTips);
				dTips.className = "mod-at-wrap";
				dDiv = dTips.appendChild(dTips.cloneNode(false));
				dDiv.className = "mod-at";
				dDiv = dDiv.appendChild(dTips.cloneNode(false));
				dDiv.className = "mod-at-list";
				dUl = dDiv.appendChild(dUl);
				dUl.className = "at-user-list";
				dTips.style.display = "none";
				dEmail.parentNode.appendChild(dFrag);
				M.addListener(dTips, {
					mouseenter: function() {
						this.isEnter = 1
					},
					mouseleave: function() {
						this.isEnter = 0
					}
				});
				dEmail.dSuggest = dTips;
				setInterval(function() {
					var sValue = dEmail.value,
					sTips = dEmail.dSuggest;
					if (dEmail.sCacheValue === sValue) {
						return
					} else {
						dEmail.sCacheValue = sValue
					}
					if (!sValue) {
						dTips.style.display = "none";
						return
					}
					var aValue = sValue.split("@"),
					dFrag = document.createDocumentFragment(),
					l = aSuffix.length,
					sSuffix;
					sInputSuffix = ["@", aValue[1]].join("");
					for (var i = 0; i < l; i++) {
						sSuffix = aSuffix[i];
						if (aValue[1] && ("" != aValue[1]) && (sSuffix.indexOf(aValue[1]) !== 1) || (sSuffix === sInputSuffix)) {
							continue
						}
						var dLi = dLi ? dLi.cloneNode(false) : document.createElement("li"),
						dA = dA ? dA.cloneNode(false) : document.createElement("a"),
						dSpan = dSpan ? dSpan.cloneNode(false) : document.createElement("span"),
						dText = dText ? dText.cloneNode(false) : document.createTextNode("");
						dText.nodeValue = [aValue[0], sSuffix].join("");
						dSpan.appendChild(dText);
						dA.appendChild(dSpan);
						dLi.appendChild(dA);
						dLi.onclick = (function(dInput, sValue, sSuffix) {
							return function(e) {
								dInput.value = [sValue, sSuffix].join("");
								dTips.isEnter = 0;
								dInput.onblur();
								return false
							}
						})(dEmail, aValue[0], sSuffix);
						dFrag.appendChild(dLi)
					}
					if (dLi) {
						dUl.innerHTML = "";
						dUl.appendChild(dFrag);
						dTips.style.display = "";
						$(dUl).find("li").hover(function() {
							$(this).addClass("hover")
						},
						function() {
							$(this).removeClass("hover")
						})
					} else {
						dTips.style.display = "none"
					}
				},
				200)
			}
		},
		password: {
			focus: function() {
				this.className = "q-txt-focus"
			},
			blur: function() {
				this.className = "q-txt";
				var dWeight = this.parentModel.childModels.password_weight[0],
				sValue = this.value + "",
				nL = sValue.length;
				var min = 6,
				max = 15;
				if (nL < min) {
					dWeight.style.display = "none";
					tips.error(this, L("PUBLIC_PASSWORD_TIPES_MIN", {
						sum: min
					}));
					this.bIsValid = false
				} else {
					if (nL > max) {
						dWeight.style.display = "none";
						tips.error(this, L("PUBLIC_PASSWORD_TIPES_MAX", {
							sum: max
						}));
						this.bIsValid = false
					} else {
						tips.clear(this);
						dWeight.style.display = "";
						this.bIsValid = true;
						this.parentModel.childEvents.repassword[0].onblur()
					}
				}
			},
			keyup: function() {
				this.value = this.value.replace(/^\s+|\s+$/g, "")
			},
			load: function() {
				this.value = "";
				this.className = "q-txt";
				var dPwd = this,
				dWeight = this.parentModel.childModels.password_weight[0],
				aLevel = ["psw-state-empty", "psw-state-poor", "psw-state-normal", "psw-state-strong"];
				setInterval(function() {
					var sValue = dPwd.value;
					if (dPwd.sCacheValue === sValue) {
						return
					} else {
						dPwd.sCacheValue = sValue
					}
					if (!sValue) {
						dWeight.className = aLevel[0];
						dWeight.setAttribute("className", aLevel[0]);
						return
					}
					var nL = sValue.length;
					if (nL < 6) {
						dWeight.className = aLevel[0];
						dWeight.setAttribute("className", aLevel[0]);
						return
					}
					var nLFactor = Math.floor(nL / 10) ? 1 : 0;
					var nMixFactor = 0;
					sValue.match(/[a-zA-Z]+/) && nMixFactor++;
					sValue.match(/[0-9]+/) && nMixFactor++;
					sValue.match(/[^a-zA-Z0-9]+/) && nMixFactor++;
					nMixFactor > 1 && nMixFactor--;
					dWeight.className = aLevel[nLFactor + nMixFactor];
					dWeight.setAttribute("className", aLevel[nLFactor + nMixFactor])
				},
				200)
			}
		},
		repassword: {
			focus: function() {
				this.className = "q-txt-focus"
			},
			keyup: function() {
				this.value = this.value.replace(/^\s+|\s+$/g, "")
			},
			blur: function() {
				this.className = "q-txt";
				var sPwd = this.parentModel.childEvents.password[0].value,
				sRePwd = this.value;
				if (!sRePwd) {
					tips.error(this, L("PUBLIC_PLEASE_PASSWORD_ON"));
					this.bIsValid = false
				} else {
					if (sPwd !== sRePwd) {
						tips.error(this, L("PUBLIC_PASSWORD_ISDUBLE_NOT"));
						this.bIsValid = false
					} else {
						tips.success(this);
						this.bIsValid = true
					}
				}
			},
			load: function() {
				this.className = "q-txt"
			}
		},
		radio: {
			click: function() {
				this.onblur()
			},
			blur: function() {
				var sName = this.name,
				oRadio = this.parentModel.elements.sex,
				oArgs = M.getEventArgs(oRadio[0]),
				dRadio,
				nL = oRadio.length,
				bIsValid = false,
				dLastRadio = oRadio[nL - 1];
				for (var i = 0; i < nL; i++) {
					dRadio = oRadio[i];
					if (dRadio.checked) {
						bIsValid = true;
						break
					}
				}
				if (bIsValid) {
					tips.clear(dLastRadio.parentNode)
				} else {
					tips.error(dLastRadio.parentNode, oArgs.error)
				}
				for (var i = 0; i < nL; i++) {
					oRadio[i].bIsValid = bIsValid
				}
			}
		},
		checkbox: {
			click: function() {
				this.onblur()
			},
			blur: function() {
				var oArgs = M.getEventArgs(this);
				if (this.checked) {
					tips.clear(this.parentNode);
					this.bIsValid = true
				} else {
					tips.error(this.parentNode, oArgs.error);
					this.bIsValid = false
				}
			}
		},
		submit_btn: {
			click: function() {
				var args = M.getEventArgs(this);
				if (args.info && !confirm(args.info)) {
					return false
				}
				try { (function(node) {
						var parent = node.parentNode;
						if ("FORM" === parent.nodeName) {
							if ("false" === args.ajax) { (("function" !== typeof parent.onsubmit) || (false !== parent.onsubmit())) && parent.submit()
							} else {
								ajaxSubmit(parent)
							}
						} else {
							if (1 === parent.nodeType) {
								arguments.callee(parent)
							}
						}
					})(this)
				} catch(e) {
					return true
				}
				return false
			}
		},
		sendBtn: {
			click: function() {
				var parent = this.parentModel;
				return false
			}
		}
	});
	var tips = {
		init: function(D) {
			this._initError(D);
			this._initSuccess(D)
		},
		error: function(D, txt) {
			this.init(D);
			D.dSuccess.style.display = "none";
			D.dError.style.display = "";
			D.dErrorText.nodeValue = txt
		},
		success: function(D) {
			this.init(D);
			D.dError.style.display = "none";
			D.dSuccess.style.display = ""
		},
		clear: function(D) {
			this.init(D);
			D.dError.style.display = "none";
			D.dSuccess.style.display = "none"
		},
		_initError: function(D) {
			if (!D.dError || !D.dErrorText) {
				var dFrag = document.createDocumentFragment(),
				dText = document.createTextNode(""),
				dB = document.createElement("b"),
				dSpan = document.createElement("span"),
				dDiv = document.createElement("div");
				D.dError = dFrag.appendChild(dDiv);
				dDiv.className = "box-ver";
				dDiv.style.display = "none";
				dDiv.appendChild(dSpan);
				dSpan.appendChild(dB);
				dB.className = "ico-error";
				D.dErrorText = dSpan.appendChild(dText);
				var dParent = D.parentNode;
				dNext = D.nextSibling;
				if (dNext) {
					dParent.insertBefore(dFrag, dNext)
				} else {
					dParent.appendChild(dFrag)
				}
			}
		},
		_initSuccess: function(D) {
			if (!D.dSuccess) {
				var dFrag = document.createDocumentFragment(),
				dSpan = document.createElement("span");
				D.dSuccess = dFrag.appendChild(dSpan);
				dSpan.className = "ico-ok";
				dSpan.style.display = "none";
				var dParent = D.parentNode;
				dNext = D.nextSibling;
				if (dNext) {
					dParent.insertBefore(dFrag, dNext)
				} else {
					dParent.appendChild(dFrag)
				}
			}
		}
	};
	window.tips = tips
})();

$(document).ready(function() {
	var j = document,
	c = j.getElementsByTagName("input"),
	f = j.getElementsByTagName("textarea"),
	a = "placeholder" in j.createElement("input"),
	k = "placeholder" in j.createElement("textarea"),
	h = function(m) {
		var n = m.getAttribute("placeholder"),
		i = m.defaultValue;
		if (i == "") {
			m.value = n
		}
		m.onfocus = function() {
			if (m.value === n) {
				this.value = ""
			}
		};
		m.onblur = function() {
			if (m.value === "") {
				this.value = n
			}
		}
	};
	if (!a) {
		for (var b = 0,
		d = c.length; b < d; b++) {
			var e = c[b],
			l = e.getAttribute("placeholder");
			if (e.type === "text" && l) {
				h(e)
			}
		}
	}
	if (!k) {
		for (var b = 0,
		d = f.length; b < d; b++) {
			var g = f[b],
			l = g.getAttribute("placeholder");
			if (g.type === "textarea" && l) {
				h(g)
			}
		}
	}
});