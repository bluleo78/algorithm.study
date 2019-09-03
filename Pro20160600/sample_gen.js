let i = 0;
let n = 300;
const cnt = n * (n - n/2) /2;
console.log(1)
console.log(n)
console.log(cnt)
for(i = 0; i < n * (n - n/2) / 2; i++) {
   const i0 = Math.floor(Math.random() * n) + 1;
   let i1 = Math.floor(Math.random() * n) + 1;
   while (i0 === i1) {
      i1 = Math.floor(Math.random() * n) + 1;
   }
   const c = Math.floor(Math.random() * (100000 - 1)) + 1;
   console.log(i0 + " " + i1 + " " +  c);
}
