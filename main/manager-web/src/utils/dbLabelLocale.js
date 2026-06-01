const CJK_RE = /[\u4e00-\u9fff\u3400-\u4dbf]/;

export function hasCjk(text) {
  if (!text || typeof text !== 'string') return false;
  return CJK_RE.test(text);
}

function getExactLabel(i18n, text) {
  const labels = i18n.messages[i18n.locale]?.dbLabels;
  if (labels && Object.prototype.hasOwnProperty.call(labels, text)) {
    return labels[text];
  }
  return null;
}

function applyPatterns(i18n, text) {
  const patterns = i18n.messages[i18n.locale]?.dbLabelPatterns;
  if (!patterns || !patterns.length) return text;
  let result = text;
  patterns.forEach(([from, to]) => {
    if (from && result.includes(from)) {
      result = result.split(from).join(to);
    }
  });
  return result;
}

/**
 * Localize display names loaded from MySQL (Chinese defaults) for vi/en UI.
 */
export function localizeDbLabel(i18n, text) {
  if (text == null || text === '') return text;
  const str = String(text);
  if (!hasCjk(str)) return str;

  const exact = getExactLabel(i18n, str);
  if (exact) return exact;

  const patterned = applyPatterns(i18n, str);
  return patterned;
}

function applySystemPromptPatterns(i18n, text) {
  const patterns = i18n.messages[i18n.locale]?.systemPromptPatterns;
  if (!patterns || !patterns.length) return text;
  let result = text;
  patterns.forEach(([from, to]) => {
    if (from && result.includes(from)) {
      result = result.split(from).join(to);
    }
  });
  return result;
}

function resolveSystemPromptTemplate(i18n, text, agentName) {
  const prompts = i18n.messages[i18n.locale]?.systemPrompts;
  if (!prompts) return null;

  if (agentName && prompts[agentName]) {
    return prompts[agentName];
  }

  const str = String(text || '');
  const fuzzyKeys = ['', '', '', '', ''];
  for (const key of fuzzyKeys) {
    if (prompts[key] && (str.includes(key) || (agentName && String(agentName).includes(key)))) {
      return prompts[key];
    }
  }
  if (str.includes('') && prompts['']) {
    return prompts[''];
  }
  return null;
}

/**
 * Localize default template system prompts for vi/en UI.
 */
export function localizeSystemPrompt(i18n, text, agentName) {
  if (text == null || text === '') return text;

  const template = resolveSystemPromptTemplate(i18n, text, agentName);
  if (template) return template;

  if (!hasCjk(text)) return text;
  return applySystemPromptPatterns(i18n, text);
}

/** Localize agent name + system prompt fields after loading from API/DB. */
export function localizeAgentFormFields(i18n, fields) {
  const rawName = fields.agentName;
  const result = { ...fields };
  if (result.agentName) {
    result.agentName = localizeDbLabel(i18n, result.agentName);
  }
  if (result.systemPrompt) {
    result.systemPrompt = localizeSystemPrompt(i18n, result.systemPrompt, rawName);
  }
  if (result.summaryMemory && hasCjk(result.summaryMemory)) {
    result.summaryMemory = applySystemPromptPatterns(i18n, result.summaryMemory);
  }
  return result;
}

export function installDbLabelLocale(Vue, i18n) {
  Vue.prototype.$dbLabel = function dbLabel(text) {
    return localizeDbLabel(i18n, text);
  };
  Vue.prototype.$dbSystemPrompt = function dbSystemPrompt(text, agentName) {
    return localizeSystemPrompt(i18n, text, agentName);
  };
}
