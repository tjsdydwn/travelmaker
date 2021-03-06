$(function () {
    //클래스
    const {getEl, addEvent, useState, getFormData, showLoading, closeLoading} = new travelmaker.utils();
    const editor = new travelmaker.editor();
    const modal = new travelmaker.modal('#modal');
    const ajax = new travelmaker.ajax();
    const t = new travelmaker.template();

    //변수
    const hiddenRno = getEl('#rno');
    const title = getEl('#title');
    const btnMainImage = getEl('.btn-main-image');
    const inputFile = getEl('#input-file');
    const editorTitle = getEl('.editor-title');
    const btnPhoto = getEl('.btn-photo');
    const btnMap = getEl('.btn-map');
    const btnHr = getEl('.btn-hr');
    const btnVideo = getEl('.btn-video');
    const inputHash = getEl('#input-hash');
    const btnHash = getEl('#btn-hash');
    const hashView = getEl('.hash-view');
    const staticMapContainer = getEl('#map-container');
    const btnSave = getEl('#btn-save');

    let hashNodeList = [];  //해쉬태그 모음
    let getMapData; //지도 정보를 확인할 함수
    let map; // 카카오 또는 구글 지도

    const [setEssay, getEssay] = useState({});
    const $editor = $('#editor');

    addEvent(title, 'change', (e) => setEssay({title: e.target.value}));
    addEvent(btnHr, 'click', () => $editor.summernote('insertNode', document.createElement('hr')));
    addEvent(btnVideo, 'click', () => $editor.summernote('videoDialog.show'));
    addEvent(btnPhoto, 'click', () => $editor.summernote('imageDialog.show'));
    addEvent(btnHash, 'click', () => {
        if (inputHash.value) addHashSpan(inputHash.value);
    });

    addEvent(inputHash, 'keyup', (e) => {
        if (e.keyCode === 13) btnHash.click();
    });

    addEvent(btnMap, 'click', () => {
        $editor.summernote('saveRange');
        if (!getEssay().isDomestic) {
            modal.createCustom(t.gmap(), () => {
                map = new travelmaker.googleMap(getEl('#map'));
                getMapData = map.create(modal, insertStaticMap);
            });
        } else {
            modal.createCustom(t.kmap(), () => {
                map = new travelmaker.kakaoMap(getEl('#map'));
                getMapData = map.create(modal, insertStaticMap);
            })
        }
    });

    addEvent(inputFile, 'change', (e) => {
        if (!e.target.files.length) return;
        const image = e.target.files[0];
        const fr = new FileReader();
        fr.readAsDataURL(image);
        fr.onload = () => {
            editorTitle.style.backgroundImage = `url("${fr.result}")`;
            setEssay({'imageFile': image});
        };
    });

    addEvent(btnMainImage, 'click', () => inputFile.click());

    addEvent(btnSave, 'click', () => {
        beforeSaveSetEssay();
        ajax.updateEssay(getEssay().rno, getFormData(getEssay()))
            .then(ret => {
                if (ret) {
                    alert('정상적으로 저장되었씁니다.');
                    location.href = '/';
                }
            })
            .catch(console.error);
    });
    //로드 끝나자 마자 호출할 함수
    initOnLoad();

    function initOnLoad() {
        showLoading();
        $editor.summernote({
            ...editor.summernote,
            callback: {
                onInit: function () {
                    title.focus();
                }
            }
        });

        ajax.getEssay(hiddenRno.value).then(({data}) => {
            setEditorFromTemp(data);
            setEssay(data);
            closeLoading();
        }).catch(console.error)
    }

    function insertStaticMap() {
        const mapElement = map.getStaticMap(staticMapContainer, getMapData());
        $editor.summernote('restoreRange');
        $editor.summernote('insertNode', mapElement);
        staticMapContainer.innerHTML = '';
    }

    function getHashTagString() {
        let hashTagList = [];
        hashNodeList.forEach(hashNode => {
            hashTagList.push(hashNode.innerText);
        });
        return hashTagList.join(',');
    }

    function beforeSaveSetEssay() {
        setEssay({
            content: $editor.summernote('code'),
            hashtag: getHashTagString()
        })
    }

    function addHashSpan(text) {
        hashView.innerHTML = '';

        const span = document.createElement('span');
        span.classList.add('hash');
        span.innerText = text;
        addEvent(span, 'click', (e) => {
            hashNodeList = hashNodeList.filter(node => node !== e.target);
            e.target.remove();
        });

        inputHash.value = '';
        hashNodeList.push(span);
        hashNodeList.forEach(hashNode => hashView.appendChild(hashNode));
    }

    function setEditorFromTemp(data) {
        hiddenRno.value = data.rno;
        title.value = data.title;
        $editor.summernote('code', data.content);
        editorTitle.style.backgroundImage = `url(${data.imageName})`;
        if (data.hashtag) data.hashtag.split(',').forEach(addHashSpan);
    }
});