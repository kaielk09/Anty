const toggle = document.querySelector(".menu-toggle");
const nav = document.querySelector(".nav");

toggle?.addEventListener("click", () => {
  const open = nav.classList.toggle("open");
  toggle.setAttribute("aria-expanded", String(open));
});

nav?.querySelectorAll("a").forEach((link) => {
  link.addEventListener("click", () => {
    nav.classList.remove("open");
    toggle?.setAttribute("aria-expanded", "false");
  });
});

const revealItems = document.querySelectorAll(".feature-card, .loader-card, .download-panel");
if ("IntersectionObserver" in window) {
  const observer = new IntersectionObserver((entries) => {
    entries.forEach((entry) => {
      if (entry.isIntersecting) {
        entry.target.classList.add("visible");
        observer.unobserve(entry.target);
      }
    });
  }, { threshold: 0.12 });
  revealItems.forEach((item) => {
    item.classList.add("reveal");
    observer.observe(item);
  });
}

// Interactive Purple ClickGUI preview
(() => {
  const tabs = [...document.querySelectorAll(".demo-tab")];
  const modules = [...document.querySelectorAll(".demo-module")];
  const name = document.getElementById("demo-category-name");
  const heading = document.getElementById("demo-heading");
  const subheading = document.getElementById("demo-subheading");
  const combatNote = document.getElementById("demo-combat-note");
  const moduleGrid = document.getElementById("demo-modules");
  const search = document.getElementById("demo-search");
  const configTitle = document.getElementById("demo-config-title");
  const configDescription = document.getElementById("demo-config-description");
  const accent = document.getElementById("demo-accent");
  const scale = document.getElementById("demo-scale");
  const scaleValue = document.getElementById("demo-scale-value");
  const opacity = document.getElementById("demo-opacity");
  const opacityValue = document.getElementById("demo-opacity-value");
  const layout = document.getElementById("demo-layout");
  if (!tabs.length || !moduleGrid) return;

  const labels = {
    all: ["ALL", "Wszystkie moduły", "Zarządzaj funkcjami i dopasuj je do swojego stylu."],
    combat: ["COMBAT", "Combat", "Ta kategoria jest zarezerwowana na przyszłość."],
    movement: ["MOVEMENT", "Movement", "Wskaźniki ruchu i informacje o poruszaniu."],
    visuals: ["VISUALS", "Visuals", "Rozbuduj HUD i dopasuj każdy detal obrazu."]
  };
  let currentCategory = "all";

  function applyFilters() {
    const query = (search?.value || "").trim().toLowerCase();
    let visible = 0;
    modules.forEach((card) => {
      const categoryMatch = currentCategory === "all" || card.dataset.category === currentCategory;
      const textMatch = !query || card.textContent.toLowerCase().includes(query);
      const show = categoryMatch && textMatch;
      card.hidden = !show;
      if (show) visible++;
    });
    moduleGrid.hidden = currentCategory === "combat";
    combatNote.hidden = currentCategory !== "combat";
  }

  tabs.forEach((tab) => tab.addEventListener("click", () => {
    currentCategory = tab.dataset.category;
    tabs.forEach((item) => {
      const active = item === tab;
      item.classList.toggle("active", active);
      item.setAttribute("aria-selected", String(active));
    });
    const copy = labels[currentCategory];
    name.textContent = copy[0];
    heading.textContent = copy[1];
    subheading.textContent = copy[2];
    if (search) search.value = "";
    applyFilters();
  }));

  modules.forEach((card) => {
    const toggle = card.querySelector(".demo-switch");
    const config = card.querySelector(".demo-config");
    toggle?.addEventListener("click", () => {
      const enabled = toggle.getAttribute("aria-pressed") !== "true";
      toggle.setAttribute("aria-pressed", String(enabled));
      card.classList.toggle("enabled", enabled);
      saveModuleState(card.querySelector(".demo-module-copy b")?.textContent || "Moduł", enabled);
    });
    config?.addEventListener("click", () => {
      const title = card.querySelector(".demo-module-copy b")?.textContent || "Moduł";
      showModuleConfig(title, card.querySelector(".demo-module-copy small")?.textContent || "Ustawienia modułu");
    });
  });


  // Expanded Visuals library: visual-only modules with individual configuration.
  const visualLibrary = [
    ["Armor Durability","Wytrzymałość każdej części zbroi","⬡"],
    ["Custom Sky","Własne niebo, gwiazdy i kolory","☾"],
    ["Sky Presets","Gotowe style nieba","✦"],
    ["Weather Changer","Deszcz, śnieg i pogoda","☁"],
    ["Custom Fog","Kolor, dystans i gęstość mgły","≋"],
    ["Ambient Particles","Świetliki, iskry i płatki śniegu","✧"],
    ["Item Physics","Wygląd i obrót leżących przedmiotów","◇"],
    ["Totem Animation","Animacja i skala totemu","✥"],
    ["Damage Tint","Kolorystyczny efekt otrzymania obrażeń","▧"],
    ["Jump Circle","Efekt pierścienia przy lądowaniu","◎"],
    ["Player Trails","Kolorowe ślady ruchu","〰"],
    ["Player Model HUD","Model postaci na ekranie","♙"],
    ["Name Tags","Wygląd etykiet graczy","Aa"],
    ["Custom Hotbar","Własny pasek szybkiego wyboru","▤"],
    ["Inventory HUD","Podgląd ekwipunku","▦"],
    ["Custom Scoreboard","Personalizacja tablicy wyników","☷"],
    ["Tab List","Wygląd listy graczy","☰"],
    ["Chat Customizer","Kolory i tło czatu","▣"],
    ["Low Fire","Zmniejszony i konfigurowalny ogień","♨"],
    ["ViewModel","Pozycja, obrót i skala przedmiotów","◩"],
    ["Crosshair Dot","Dodatkowa kropka celownika","⊙"],
    ["Enchantment Glint","Kolor i intensywność połysku","✧"],
    ["Block Overlay","Kontur i wypełnienie wskazanego bloku","▧"],
    ["Time Changer","Pora dnia i kolor światła","◷"],
    ["Color Grading","Filtry i nasycenie obrazu","◐"]
  ];
  const existingNames = new Set(modules.map(card => card.querySelector(".demo-module-copy b")?.textContent));
  visualLibrary.forEach(([title, description, symbol]) => {
    if (existingNames.has(title)) return;
    const card = document.createElement("article");
    card.className = "demo-module";
    card.dataset.category = "visuals";
    card.innerHTML = '<div class="demo-module-icon"></div><div class="demo-module-copy"><b></b><small></small></div><button class="demo-switch" aria-label="Przełącz moduł" aria-pressed="false"></button><button class="demo-config" aria-label="Konfiguruj moduł">⚙</button>';
    card.querySelector(".demo-module-icon").textContent = symbol;
    card.querySelector(".demo-module-copy b").textContent = title;
    card.querySelector(".demo-module-copy small").textContent = description;
    card.querySelector(".demo-switch").setAttribute("aria-label", "Przełącz " + title);
    card.querySelector(".demo-config").setAttribute("aria-label", "Konfiguruj " + title);
    moduleGrid.appendChild(card);
    modules.push(card);
    const toggle = card.querySelector(".demo-switch");
    toggle.addEventListener("click", () => {
      const enabled = toggle.getAttribute("aria-pressed") !== "true";
      toggle.setAttribute("aria-pressed", String(enabled));
      card.classList.toggle("enabled", enabled);
      saveModuleState(title, enabled);
    });
    card.querySelector(".demo-config").addEventListener("click", () => showModuleConfig(title, description));
  });

  const storageKey = "purple-client-visual-settings-v1";
  let savedSettings = {};
  try { savedSettings = JSON.parse(localStorage.getItem(storageKey) || "{}"); } catch (_) {}
  function saveModuleState(title, enabled) {
    savedSettings[title] = {...(savedSettings[title] || {}), enabled};
    localStorage.setItem(storageKey, JSON.stringify(savedSettings));
  }
  function showModuleConfig(title, description) {
    configTitle.textContent = title + " — konfiguracja";
    configDescription.textContent = description + ". Zmiany są zapisywane lokalnie w tej przeglądarce.";
    const controls = document.querySelector(".demo-config-controls");
    if (!controls) return;
    const common = [
      ["Skala","range",50,200,100,"%"],
      ["Pozycja X","range",0,100,50,"%"],
      ["Pozycja Y","range",0,100,50,"%"],
      ["Przezroczystość","range",0,100,95,"%"],
      ["Kolor","color",null,null,"#a978ff",""],
      ["Styl","select",null,null,["Purple","Classic","Minimal","Gradient"],""]
    ];
    const lower = title.toLowerCase();
    const extra = [];
    if (lower.includes("sky") || lower.includes("nieb") || lower.includes("fog") || lower.includes("weather") || lower.includes("time")) {
      extra.push(["Preset","select",null,null,["Purple Dusk","Aurora","Midnight","Sunset","Vanilla"],""]);
      extra.push(["Intensywność","range",0,100,75,"%"]);
      extra.push(["Animacja","select",null,null,["Off","Slow","Normal","Smooth"],""]);
    }
    if (lower.includes("armor")) {
      extra.push(["Pokaż procent","checkbox",null,null,true,""]);
      extra.push(["Pokaż durability bar","checkbox",null,null,true,""]);
      extra.push(["Układ","select",null,null,["Horizontal","Vertical","Compact"],""]);
    }
    if (lower.includes("crosshair")) {
      extra.push(["Rozmiar","range",1,20,6,"px"]);
      extra.push(["Grubość","range",1,8,2,"px"]);
      extra.push(["Gap","range",0,15,3,"px"]);
    }
    if (lower.includes("viewmodel")) {
      extra.push(["Offset X","range",-100,100,0,""]);
      extra.push(["Offset Y","range",-100,100,0,""]);
      extra.push(["Offset Z","range",-100,100,0,""]);
      extra.push(["Rotacja","range",-180,180,0,"°"]);
    }
    if (lower.includes("particle") || lower.includes("trail") || lower.includes("circle") || lower.includes("animation") || lower.includes("physics")) {
      extra.push(["Prędkość","range",0,100,50,"%"]);
      extra.push(["Gęstość","range",1,100,35,"%"]);
      extra.push(["Typ efektu","select",null,null,["Soft","Glow","Spark","Pixel","Smooth"],""]);
    }
    if (lower.includes("hud") || lower.includes("hotbar") || lower.includes("scoreboard") || lower.includes("tab") || lower.includes("chat") || lower.includes("name")) {
      extra.push(["Zaokrąglenie","range",0,20,8,"px"]);
      extra.push(["Tło","select",null,null,["Solid","Glass","Blur","Outline"],""]);
      extra.push(["Cień","checkbox",null,null,true,""]);
    }
    const definitions = [...common, ...extra];
    controls.innerHTML = "";
    definitions.forEach(([label,type,min,max,value,suffix]) => {
      const wrapper = document.createElement("label");
      wrapper.className = "visual-setting";
      const caption = document.createElement("span");
      caption.textContent = label;
      wrapper.appendChild(caption);
      let input;
      if (type === "select") {
        input = document.createElement("select");
        value.forEach(option => { const opt=document.createElement("option"); opt.textContent=option; input.appendChild(opt); });
      } else {
        input = document.createElement("input");
        input.type = type;
        if (type === "range") { input.min=min; input.max=max; input.value=value; }
        if (type === "color") input.value=value;
        if (type === "checkbox") input.checked=value;
      }
      const key = title + ":" + label;
      const saved = savedSettings[key];
      if (saved !== undefined) {
        if (type === "checkbox") input.checked = saved;
        else input.value = saved;
      }
      const output = document.createElement("output");
      if (type === "range") output.textContent = input.value + suffix;
      if (type === "checkbox") output.textContent = input.checked ? "ON" : "OFF";
      if (type === "color") output.textContent = input.value.toUpperCase();
      input.addEventListener("input", () => {
        const current = type === "checkbox" ? input.checked : input.value;
        savedSettings[key] = current;
        localStorage.setItem(storageKey, JSON.stringify(savedSettings));
        if (type === "range") output.textContent = input.value + suffix;
        if (type === "checkbox") output.textContent = input.checked ? "ON" : "OFF";
        if (type === "color") output.textContent = input.value.toUpperCase();
      });
      wrapper.appendChild(input);
      wrapper.appendChild(output);
      controls.appendChild(wrapper);
    });
  }
  modules.forEach(card => {
    const title = card.querySelector(".demo-module-copy b")?.textContent || "";
    const saved = savedSettings[title];
    if (saved?.enabled) {
      const toggle = card.querySelector(".demo-switch");
      toggle?.setAttribute("aria-pressed","true");
      card.classList.add("enabled");
    }
  });

  search?.addEventListener("input", applyFilters);
  accent?.addEventListener("change", () => {
    const colors = {violet:"#a978ff",blue:"#69a9ff",rose:"#ed87bc",mint:"#73d6b0"};
    document.documentElement.style.setProperty("--purple", colors[accent.value] || colors.violet);
  });
  scale?.addEventListener("input", () => { scaleValue.textContent = scale.value + "%"; });
  opacity?.addEventListener("input", () => { opacityValue.textContent = opacity.value + "%"; document.querySelector(".clickgui-demo").style.opacity = String(Number(opacity.value)/100); });
  layout?.addEventListener("change", () => {
    moduleGrid.style.gap = layout.value === "Compact" ? "5px" : layout.value === "Minimal" ? "12px" : "8px";
    modules.forEach(card => card.style.minHeight = layout.value === "Compact" ? "48px" : layout.value === "Minimal" ? "64px" : "56px");
  });
  applyFilters();
})();
