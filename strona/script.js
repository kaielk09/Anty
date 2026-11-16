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
    });
    config?.addEventListener("click", () => {
      const title = card.querySelector(".demo-module-copy b")?.textContent || "Moduł";
      configTitle.textContent = title + " — konfiguracja";
      configDescription.textContent = "Podgląd ustawień. Zmiany poniżej dotyczą wyglądu demonstracji.";
    });
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
