let demo = {};

document.addEventListener('DOMContentLoaded', function () {
    let canvas = document.getElementById('canvas');
    let ctx = canvas.getContext('2d');
    let elem = {
        select: document.getElementById('select'),
        back: document.getElementById('background'),
        size: document.getElementById('size'),
        output: document.getElementById('output')
    };

    fetch('../src/main/resources/image-names.txt')
    // fetch('http://localhost:63342/emoji-art/src/main/resources/image-names.txt?_ijt=81jr6qp25d8mo1m7btpcuf62su')
        .then(res => res.text())
        .then(text => {
            let names = text.split('\n');
            names.sort();
            for (let i = 0; i < names.length; i++) {
                let opt = document.createElement('option');
                opt.value = names[i];
                opt.innerHTML = names[i];
                elem.select.appendChild(opt);
            }
            demo.convert();
        });

    demo.key = function (event) {
        if (event.keyCode === 13) {
            demo.convert();
        }
    };

    demo.convert = function () {
        let name = elem.select.selectedOptions[0].value;
        let img = new Image();
        img.onload = function () {
            let back = elem.back.value || '#ffffff';
            let width = parseInt(elem.size.value) || 100;
            ctx.fillStyle = back;
            ctx.fillRect(0, 0, 64, 64);
            ctx.drawImage(img, 0, 0);
            let bmp = Canvas2Image.saveAsRawBMP(canvas);
            elem.output.innerHTML = img2txt(bmp, ['-f', 'html', '-W', width, 'test.bmp']);
        };
        img.src = '../src/main/resources/images/' + encodeURIComponent(name) + '.png';
    };
});
