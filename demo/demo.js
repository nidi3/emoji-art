var demo = {};

document.addEventListener('DOMContentLoaded', function () {
    var canvas = document.getElementById('canvas');
    var ctx = canvas.getContext('2d');
    var elem = {
        select: document.getElementById('select'),
        back: document.getElementById('background'),
        size: document.getElementById('size'),
        output: document.getElementById('output')
    };

    fetch('../src/main/resources/image-names.txt')
    // fetch('http://localhost:63342/emoji-art/src/main/resources/image-names.txt?_ijt=23p1m541fqbc1gt69jbuevdm77')
        .then(res => res.text())
        .then(text => {
            var names = text.split('\n');
            names.sort();
            for (var i = 0; i < names.length; i++) {
                var opt = document.createElement('option');
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
        var name = elem.select.selectedOptions[0].value;
        var img = new Image();
        img.onload = function () {
            var back = elem.back.value || '#ffffff';
            var width = parseInt(elem.size.value) || 100;
            ctx.fillStyle = back;
            ctx.fillRect(0, 0, 64, 64);
            ctx.drawImage(img, 0, 0);
            var bmp = Canvas2Image.saveAsRawBMP(canvas);
            var txt = img2txt(bmp, ['-f', 'html', '-W', width, 'test.bmp']);
            elem.output.innerHTML = txt;
        };
        img.src = '../src/main/resources/images/' + encodeURIComponent(name) + '.png';
    };
});
