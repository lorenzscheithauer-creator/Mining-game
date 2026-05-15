const state = {
  credits: 75,
  ore: 0,
  depth: 0,
  heat: 0,
  drillLevel: 1,
  crewLevel: 0,
  scannerLevel: 0,
  minedForContract: 0,
  particles: [],
  lastAutoMine: 0,
};

const layers = [
  { name: "Kupferader", at: 0, color: "#c87533", value: 3 },
  { name: "Silberbruch", at: 80, color: "#cbd5e1", value: 6 },
  { name: "Smaragdhöhle", at: 190, color: "#34d399", value: 11 },
  { name: "Rubinkern", at: 340, color: "#fb7185", value: 18 },
  { name: "Sternenobsidian", at: 540, color: "#8b5cf6", value: 30 },
];

const contracts = [
  { ore: 50, reward: 120, title: "Auftrag: 50 Erz liefern" },
  { ore: 140, reward: 420, title: "Auftrag: Schmelzwerk versorgen" },
  { ore: 320, reward: 1250, title: "Auftrag: Tiefenbasis bauen" },
  { ore: 720, reward: 3600, title: "Auftrag: Orbit-Fracht beladen" },
];

const elements = {
  credits: document.querySelector("#credits"),
  ore: document.querySelector("#ore"),
  depth: document.querySelector("#depth"),
  layerName: document.querySelector("#layerName"),
  heat: document.querySelector("#heat"),
  contractTitle: document.querySelector("#contractTitle"),
  contractReward: document.querySelector("#contractReward"),
  contractProgress: document.querySelector("#contractProgress"),
  drillLevel: document.querySelector("#drillLevel"),
  crewLevel: document.querySelector("#crewLevel"),
  scannerLevel: document.querySelector("#scannerLevel"),
  mineButton: document.querySelector("#mineButton"),
  sellButton: document.querySelector("#sellButton"),
  coolButton: document.querySelector("#coolButton"),
  drillButton: document.querySelector("#drillButton"),
  crewButton: document.querySelector("#crewButton"),
  scannerButton: document.querySelector("#scannerButton"),
  logList: document.querySelector("#logList"),
  canvas: document.querySelector("#mineCanvas"),
};

const ctx = elements.canvas.getContext("2d");
let contractIndex = 0;
let animationFrame = 0;

function format(value) {
  return Math.floor(value).toLocaleString("de-DE");
}

function activeLayer() {
  return layers.reduce((current, layer) => (state.depth >= layer.at ? layer : current), layers[0]);
}

function upgradeCost(type) {
  const costs = {
    drill: 90 * state.drillLevel ** 1.65,
    crew: 140 * (state.crewLevel + 1) ** 1.72,
    scanner: 180 * (state.scannerLevel + 1) ** 1.68,
  };
  return Math.floor(costs[type]);
}

function addLog(message) {
  const entry = document.createElement("li");
  entry.textContent = message;
  elements.logList.prepend(entry);
  while (elements.logList.children.length > 6) {
    elements.logList.lastElementChild.remove();
  }
}

function spawnParticles(amount, intense = false) {
  const layer = activeLayer();
  for (let index = 0; index < amount; index += 1) {
    state.particles.push({
      x: 330 + Math.random() * 120,
      y: 260 + Math.random() * 70,
      vx: (Math.random() - 0.5) * (intense ? 8 : 4),
      vy: -Math.random() * (intense ? 9 : 5) - 1,
      size: Math.random() * 7 + 3,
      life: Math.random() * 42 + 28,
      color: Math.random() > 0.2 ? layer.color : "#ffc857",
    });
  }
}

function mine(multiplier = 1) {
  if (state.heat >= 100) {
    addLog("Der Bohrer ist überhitzt. Kühle ihn, bevor du weiter abbaust.");
    return;
  }

  const layer = activeLayer();
  const mined = Math.ceil((state.drillLevel * 2 + state.scannerLevel + 2) * multiplier);
  const depthGain = (0.8 + state.drillLevel * 0.28) * multiplier;
  state.ore += mined;
  state.minedForContract += mined;
  state.depth += depthGain;
  state.heat = Math.min(100, state.heat + 8 + state.drillLevel * 0.65);
  spawnParticles(12, multiplier > 1);

  const nextLayer = activeLayer();
  if (nextLayer.name !== layer.name) {
    addLog(`Neue Schicht entdeckt: ${nextLayer.name}. Erz ist jetzt wertvoller.`);
    spawnParticles(34, true);
  }

  completeContracts();
  updateUI();
}

function sellOre() {
  if (state.ore <= 0) {
    addLog("Kein Erz im Lager. Baue zuerst Material ab.");
    return;
  }

  const layer = activeLayer();
  const scannerBonus = 1 + state.scannerLevel * 0.12;
  const revenue = Math.floor(state.ore * layer.value * scannerBonus);
  state.credits += revenue;
  addLog(`${format(state.ore)} Erz für ${format(revenue)} Credits verkauft.`);
  state.ore = 0;
  updateUI();
}

function coolDrill() {
  state.heat = Math.max(0, state.heat - 35);
  addLog("Kühlmittel aktiviert. Der Bohrer läuft wieder stabiler.");
  updateUI();
}

function buyUpgrade(type) {
  const cost = upgradeCost(type);
  if (state.credits < cost) {
    addLog(`Nicht genug Credits. Benötigt: ${format(cost)}.`);
    return;
  }

  state.credits -= cost;
  if (type === "drill") {
    state.drillLevel += 1;
    addLog(`Bohrer auf Level ${state.drillLevel} verbessert.`);
  }
  if (type === "crew") {
    state.crewLevel += 1;
    addLog(`Crew Level ${state.crewLevel} arbeitet nun automatisch.`);
  }
  if (type === "scanner") {
    state.scannerLevel += 1;
    addLog(`Scanner Level ${state.scannerLevel} findet bessere Adern.`);
  }
  updateUI();
}

function completeContracts() {
  const contract = contracts[contractIndex];
  if (!contract || state.minedForContract < contract.ore) {
    return;
  }

  state.credits += contract.reward;
  state.minedForContract -= contract.ore;
  addLog(`${contract.title} abgeschlossen: +${format(contract.reward)} Credits.`);
  contractIndex = Math.min(contractIndex + 1, contracts.length - 1);
}

function updateUI() {
  const contract = contracts[contractIndex];
  const layer = activeLayer();
  const drillCost = upgradeCost("drill");
  const crewCost = upgradeCost("crew");
  const scannerCost = upgradeCost("scanner");

  elements.credits.textContent = format(state.credits);
  elements.ore.textContent = format(state.ore);
  elements.depth.textContent = `${format(state.depth)} m`;
  elements.layerName.textContent = layer.name;
  elements.heat.textContent = `${Math.floor(state.heat)}%`;
  elements.contractTitle.textContent = contract.title;
  elements.contractReward.textContent = `Belohnung: ${format(contract.reward)} Credits`;
  elements.contractProgress.style.width = `${Math.min(100, (state.minedForContract / contract.ore) * 100)}%`;
  elements.drillLevel.textContent = `Level ${state.drillLevel} · ${format(drillCost)} Credits`;
  elements.crewLevel.textContent = `Level ${state.crewLevel} · ${format(crewCost)} Credits`;
  elements.scannerLevel.textContent = `Level ${state.scannerLevel} · ${format(scannerCost)} Credits`;

  elements.mineButton.disabled = state.heat >= 100;
  elements.sellButton.disabled = state.ore <= 0;
  elements.drillButton.disabled = state.credits < drillCost;
  elements.crewButton.disabled = state.credits < crewCost;
  elements.scannerButton.disabled = state.credits < scannerCost;
}

function drawBackground(width, height) {
  const sky = ctx.createLinearGradient(0, 0, 0, height);
  sky.addColorStop(0, "#15243c");
  sky.addColorStop(0.32, "#0d1525");
  sky.addColorStop(1, "#05070c");
  ctx.fillStyle = sky;
  ctx.fillRect(0, 0, width, height);

  for (let row = 0; row < 7; row += 1) {
    const y = 90 + row * 76;
    ctx.fillStyle = row % 2 === 0 ? "rgba(120, 84, 55, 0.22)" : "rgba(35, 48, 70, 0.24)";
    ctx.beginPath();
    ctx.moveTo(0, y + Math.sin(animationFrame / 40 + row) * 8);
    for (let x = 0; x <= width; x += 60) {
      ctx.lineTo(x, y + Math.sin(x / 80 + row + animationFrame / 55) * 14);
    }
    ctx.lineTo(width, height);
    ctx.lineTo(0, height);
    ctx.closePath();
    ctx.fill();
  }
}

function drawOreVeins(width, height) {
  const layer = activeLayer();
  ctx.lineWidth = 7;
  ctx.lineCap = "round";
  for (let vein = 0; vein < 8; vein += 1) {
    ctx.strokeStyle = vein % 3 === 0 ? layer.color : "rgba(255, 255, 255, 0.12)";
    ctx.beginPath();
    const startY = 130 + vein * 58;
    ctx.moveTo(-20, startY);
    for (let x = 0; x < width + 50; x += 70) {
      ctx.lineTo(x, startY + Math.sin(x / 60 + vein) * 24);
    }
    ctx.stroke();
  }
}

function drawMachine(width) {
  const bob = Math.sin(animationFrame / 11) * 4;
  const x = width / 2;
  const y = 155 + bob;

  ctx.save();
  ctx.translate(x, y);

  ctx.fillStyle = "rgba(0, 0, 0, 0.32)";
  ctx.beginPath();
  ctx.ellipse(0, 206, 92, 18, 0, 0, Math.PI * 2);
  ctx.fill();

  const bodyGradient = ctx.createLinearGradient(-65, -45, 75, 85);
  bodyGradient.addColorStop(0, "#f8fafc");
  bodyGradient.addColorStop(0.48, "#94a3b8");
  bodyGradient.addColorStop(1, "#475569");
  ctx.fillStyle = bodyGradient;
  roundRect(-66, -34, 132, 96, 28);
  ctx.fill();

  ctx.fillStyle = "#111827";
  roundRect(-42, -12, 84, 34, 14);
  ctx.fill();
  ctx.fillStyle = state.heat > 75 ? "#ff5c7a" : "#48d8ff";
  roundRect(-31, -5, 62, 20, 10);
  ctx.fill();

  ctx.strokeStyle = "#ffc857";
  ctx.lineWidth = 13;
  ctx.beginPath();
  ctx.moveTo(-62, 30);
  ctx.lineTo(-110, 84);
  ctx.moveTo(62, 30);
  ctx.lineTo(110, 84);
  ctx.stroke();

  ctx.fillStyle = "#f59e0b";
  ctx.beginPath();
  ctx.moveTo(-35, 62);
  ctx.lineTo(35, 62);
  ctx.lineTo(22, 164);
  ctx.lineTo(0, 202);
  ctx.lineTo(-22, 164);
  ctx.closePath();
  ctx.fill();

  ctx.strokeStyle = "rgba(255, 255, 255, 0.32)";
  ctx.lineWidth = 3;
  for (let stripe = 0; stripe < 4; stripe += 1) {
    ctx.beginPath();
    ctx.moveTo(-24 + stripe * 14, 78 + stripe * 22);
    ctx.lineTo(24 - stripe * 6, 88 + stripe * 18);
    ctx.stroke();
  }

  ctx.restore();
}

function roundRect(x, y, width, height, radius) {
  ctx.beginPath();
  ctx.moveTo(x + radius, y);
  ctx.arcTo(x + width, y, x + width, y + height, radius);
  ctx.arcTo(x + width, y + height, x, y + height, radius);
  ctx.arcTo(x, y + height, x, y, radius);
  ctx.arcTo(x, y, x + width, y, radius);
  ctx.closePath();
}

function drawParticles() {
  state.particles = state.particles.filter((particle) => particle.life > 0);
  for (const particle of state.particles) {
    particle.x += particle.vx;
    particle.y += particle.vy;
    particle.vy += 0.18;
    particle.life -= 1;

    ctx.globalAlpha = Math.max(0, particle.life / 60);
    ctx.fillStyle = particle.color;
    ctx.beginPath();
    ctx.arc(particle.x, particle.y, particle.size, 0, Math.PI * 2);
    ctx.fill();
    ctx.globalAlpha = 1;
  }
}

function drawCrew(width) {
  if (state.crewLevel <= 0) {
    return;
  }

  for (let member = 0; member < Math.min(state.crewLevel, 6); member += 1) {
    const x = 80 + member * 44;
    const y = 430 + Math.sin(animationFrame / 15 + member) * 6;
    ctx.fillStyle = "#ffc857";
    ctx.fillRect(x - 10, y - 17, 20, 12);
    ctx.fillStyle = "#5cf2a9";
    ctx.beginPath();
    ctx.arc(x, y, 13, 0, Math.PI * 2);
    ctx.fill();
    ctx.fillStyle = "#0f172a";
    ctx.fillRect(x - 8, y + 12, 16, 30);
  }

  ctx.fillStyle = "rgba(72, 216, 255, 0.18)";
  roundRect(width - 180, 390, 120, 64, 18);
  ctx.fill();
  ctx.fillStyle = "#48d8ff";
  ctx.font = "700 18px sans-serif";
  ctx.fillText(`Crew x${state.crewLevel}`, width - 158, 429);
}

function render() {
  animationFrame += 1;
  const { width, height } = elements.canvas;
  drawBackground(width, height);
  drawOreVeins(width, height);
  drawCrew(width);
  drawMachine(width);
  drawParticles();
  requestAnimationFrame(render);
}

function tick(time) {
  if (state.crewLevel > 0 && time - state.lastAutoMine > Math.max(450, 1800 - state.crewLevel * 120)) {
    state.lastAutoMine = time;
    if (state.heat < 96) {
      mine(0.38 + state.crewLevel * 0.05);
    }
  }

  state.heat = Math.max(0, state.heat - 0.025 * (1 + state.scannerLevel * 0.08));
  updateUI();
  requestAnimationFrame(tick);
}

elements.mineButton.addEventListener("click", () => mine(1));
elements.sellButton.addEventListener("click", sellOre);
elements.coolButton.addEventListener("click", coolDrill);
elements.drillButton.addEventListener("click", () => buyUpgrade("drill"));
elements.crewButton.addEventListener("click", () => buyUpgrade("crew"));
elements.scannerButton.addEventListener("click", () => buyUpgrade("scanner"));

if ("serviceWorker" in navigator) {
  navigator.serviceWorker.register("./service-worker.js");
}

addLog("Schacht geöffnet. Tippe auf Abbauen und erfülle den ersten Auftrag.");
updateUI();
render();
requestAnimationFrame(tick);
