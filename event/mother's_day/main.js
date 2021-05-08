var list_ham = new Array(45);
for (var k = 1; k <= 45; k++) {
  list_ham[k - 1] = k;
}

for (var k = 1; k <= 45; k++) {
  var a = Math.floor(Math.random() * 44);
  var b = Math.floor(Math.random() * 44);

  [list_ham[a], list_ham[b]] = [list_ham[b], list_ham[a]];
}

for (var k = 1; k <= 45; k++) {
    document.getElementById("container").innerHTML += "<img src=\"image/ham" + list_ham[k - 1] + ".jpg\" style=\"width: 100%\" />";
}

for (var i = 0; i < 30; i++) {
  document.getElementById("anime").innerHTML +=
    '<div class="droplet" style = "position:absolute; animation: drop ' +
    String(Math.random() * 3) +
    4 +
    "s linear; animation-fill-mode: forwards; animation-delay: 2s; top: " +
    String(Math.random() * 200) +
    "vh; left:" +
    String(Math.random() * 100) +
    'vw">💩</div>';
  document.getElementById("anime").innerHTML +=
    '<div class="droplet" style = "position:absolute; animation: drop ' +
    String(Math.random() * 3) +
    4 +
    "s linear; animation-fill-mode: forwards; animation-delay: 2s; top: " +
    String(Math.random() * 200) +
    "vh; left:" +
    String(Math.random() * 100) +
    'vw">🐹</div>';
    document.getElementById("anime").innerHTML +=
    '<div class="droplet" style = "position:absolute; animation: drop ' +
    String(Math.random() * 3) +
    4 +
    "s linear; animation-fill-mode: forwards; animation-delay: 2s; top: " +
    String(Math.random() * 200) +
    "vh; left:" +
    String(Math.random() * 100) +
    'vw">🍬</div>'
}
