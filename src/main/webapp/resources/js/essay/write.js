const setting = {
    url: `http://${location.host}`,
};

$(function () {
    const template = new travelmaker.template();
    const editor = new travelmaker.editor();
    const {setRequestHeader, useState, getJSONfromQueryString} = new travelmaker.utils();

    let hashes = [];

    let requestData = {
        rno: '',
        seq: 1,
        title: '',
        content: '',
        hashtag: '',
        fixed: 0,
        isDomestic: +getJSONfromQueryString().isDomestic
    };

    const [setEssay, getEssay] = useState(requestData);

    const $editor = $('#editor');
    const $modal = $('.modal');

    const [
        title,
        btnImage,
        btnMap,
        btnVideo,
        btnBreakLine,
        btnSaveTmp,
        boxHashTag,
        imgBackground,
        inputFileUpload,
        btnSave,
        tmpContentGroup
    ] = getEls(
        document,
        '#title',
        '#btn-image',
        '#btn-map',
        '#btn-video',
        '#btn-breakline',
        '#btn-save-tmp',
        '#box-hashtag',
        '#img-background',
        '#input-file-upload',
        '#btn-save',
        '#tmp-content-group'
    );

    inputFileUpload.addEventListener('change', inputFileUploadHandler);
    title.addEventListener('change', titleChangeHandler);
    btnImage.addEventListener('click', btnImageHandler);
    btnMap.addEventListener('click', btnMapHandler);
    btnVideo.addEventListener('click', btnVideoHandler);
    btnBreakLine.addEventListener('click', btnBreakLineHandler);
    btnSave.addEventListener('click', btnSaveHandler);
    btnSaveTmp.addEventListener('click', btnSaveTmpHandler);
    boxHashTag.addEventListener('click', hashBoxHandler);
    imgBackground.addEventListener('click', imgBackgroundHandler);

    function titleChangeHandler(e) {
        setEssay({title: this.value});
    }

    initOnLoad();

    function initOnLoad() {
        ajaxCreate(getEssay())
            .then((ret) => setEssay({rno: ret.data.rno}))
            .catch(console.error);

        ajaxGetEssayListTmp(1, 0, 'date_write')
            .then(printTmpList)
            .then(() => setTmpEvents())
            .catch(console.error);

        $editor.summernote({
            ...editor.summernote,
            callbacks: {
                onInit: function () {
                    title.focus();
                }
            }
        });
    }

    function setTmpEvents() {
        const btnGetList = document.querySelectorAll('.btn-tmp-get');
        const btnRemoveList = document.querySelectorAll('.btn-tmp-remove');

        Array.from(btnGetList).forEach(btn => {
            btn.addEventListener('click', getEssayTmpHandler);
        });
        Array.from(btnRemoveList).forEach(btn => {
            btn.addEventListener('click', deleteEssayTmpHandler)
        });
    }

    function deleteEssayTmpHandler(e) {
        const item = this.parentElement.parentElement;
        if (!confirm('해당 임시글을 삭제하시겠습니까?')) return;
        ajaxDelete(this.dataset.rno)
            .then((ret) => item.remove())
    }

    function getEssayTmpHandler(e) {
        if (!confirm('임시저장 한 글을 불러오시겠습니까?')) return;
        const item = this.parentElement.parentElement;

        ajaxGetEssay(this.dataset.rno)
            .then(({data}) => {
                setEssay(data);
                title.value = data.title || '';
                if (data.content) $editor.summernote('code', data.content);
                changeBackground(imgBackground, data.imageName);
                item.remove();
            })
            .catch(console.error);
    }

    function getBackgroundImage(el){
        let style = window.getComputedStyle(el, null);
        return style.backgroundImage.slice(5, -2);
    }

    function printTmpList({data}) {
        const $frag = $(document.createDocumentFragment());
        data.forEach(essay => {
            $frag.append(template.essayTemp(essay));
        });
        $(tmpContentGroup).append($frag);
    }

    function imgBackgroundHandler(e) {
        inputFileUpload.click();
    }

    function btnSaveTmpHandler(e) {
        setEssay({fixed: 0, content: $editor.summernote('code')});
        ajaxUpdate(getEssay())
            .then((ret) => console.log('임시저장', ret.data))
            .catch(console.error);
    }

    function btnSaveHandler(e) {
        setEssay({fixed: 1, content: $editor.summernote('code')});
        ajaxUpdate(getEssay())
            .then((ret) => console.log('발행', ret.data))
            .catch(console.error);
    }

    function btnVideoHandler(e) {
        $editor.summernote('videoDialog.show');
    }

    function hashBoxHandler(e) {
        const titleContent = '해쉬태그';
        const bodyContent = template.hashBox();
        setModal($modal, titleContent, bodyContent);
        $modal.modal('show');
        initHashtag();
    }

    function btnBreakLineHandler(e) {
        const hr = document.createElement('hr');
        $editor.summernote('insertNode', hr);
    }

    function btnMapHandler(e) {
        const {isDomestic} = getEssay();
        $editor.summernote('saveRange');
        const titleContent = '지도검색';
        const bodyContent = template.map();
        setModal($modal, titleContent, bodyContent);
        $modal.modal('show');

        if (isDomestic) initMap();
        else initGoogleMap();
    }

    function inputFileUploadHandler(e) {
        console.log(this.files[0]);
        if (!this.files[0]) return;
        const formData = getFormData(this.files[0]);
        ajaxImageUpload(getEssay().rno, formData)
            .then((ret) => {
                changeBackground(imgBackground, ret);
            })
            .catch(console.error);
    }

    function getFormData(imageFile) {
        const essay = getEssay();
        const keys = Object.keys(essay);
        const values = Object.values(essay);

        const formData = new FormData();
        formData.append('imageFile', imageFile);
        for (let i = 0; i < keys.length; i++) {
            formData.append(keys[i], values[i]);
        }
        return formData;
    }

    function changeBackground(el, imageName) {
        if (imageName) {
            el.style.backgroundImage = `url("/resources/storage/essay/${imageName}")`;
            el.innerHTML = '';
        } else {
            el.style.background = `url("/resources/img/essay-default-background.jpg")`;
            el.innerHTML = '<h3>클릭하시면, 대표이미지를 설정할 수 있어요!</h3>'
        }
    }

    function btnImageHandler(e) {
        $editor.summernote('imageDialog.show');
    }

    function initHashtag() {
        const [contentHashtag, inputHashtag, btnAddHashtag, btnAddAtModal] = getEls(
            document,
            '#content-hashtag',
            '#hashtag',
            '#btn-add-hashtag',
            '#add-at-modal'
        );

        if (hashes.length) {
            hashes.forEach(setHashContent);
        }

        btnAddHashtag.addEventListener('click', function (e) {
            if (!inputHashtag.value) return;
            hashes.push(inputHashtag.value);

            if (hashes.length) {
                contentHashtag.innerHTML = '';
                hashes.forEach(setHashContent);
            }
            inputHashtag.value = '';
            inputHashtag.focus();
            setEssay({hashtag: hashes.join(',')});
        });

        btnAddAtModal.addEventListener('click', function (e) {
            $modal.modal('hide');
            let hashText = '';
            hashes.forEach((hash) => {
                hashText += '#' + hash + ' ';
            });
            boxHashTag.innerText = hashText;
        });

        function setHashContent(hash) {
            const btn = createHashButton(hash);
            btn.addEventListener('click', function (e) {
                hashes = hashes.filter((hash) => hash !== this.innerText.slice(1));
                this.remove();
            });
            contentHashtag.appendChild(btn);
        }

        function createHashButton(hashValue) {
            const btn = document.createElement('button');
            btn.classList.add('btn');
            btn.classList.add('btn-outline-primary');
            btn.appendChild(document.createTextNode('#' + hashValue));
            return btn;
        }
    }

    function initMap() {
        let markers = [];
        const mapContainer = document.querySelector('#map'); // 지도를 표시할 div
        const mapOption = {
            center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
            level: 3 // 지도의 확대 레벨
        };

        const [keyword, btnSearchByKeyword] = getEls(
            document,
            '#keyword',
            '#btn-keyword-search'
        );

        btnSearchByKeyword.addEventListener('click', searchPlaces);

        const map = new kakao.maps.Map(mapContainer, mapOption); //지도생성
        const ps = new kakao.maps.services.Places(); //장소검색
        const infowindow = new kakao.maps.InfoWindow({zIndex: 1}); //인포윈도우

        function searchPlaces() {
            if (!keyword.value.replace(/^\s+|\s+$/g, '')) {
                alert('키워드를 입력해주세요!');
                return false;
            }
            // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
            ps.keywordSearch(keyword.value, placesSearchCB);
        }

        // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
        function placesSearchCB(data, status, pagination) {
            if (status === kakao.maps.services.Status.OK) {
                // 정상적으로 검색이 완료됐으면
                // 검색 목록과 마커를 표출합니다
                displayPlaces(data);
                // 페이지 번호를 표출합니다
                displayPagination(pagination);
            } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
                alert('검색 결과가 존재하지 않습니다.');
                return;
            } else if (status === kakao.maps.services.Status.ERROR) {
                alert('검색 결과 중 오류가 발생했습니다.');
                return;
            }
        }

        // 검색 결과 목록과 마커를 표출하는 함수입니다
        function displayPlaces(places) {
            var listEl = document.getElementById('placesList'),
                fragment = document.createDocumentFragment(),
                bounds = new kakao.maps.LatLngBounds();

            // 검색 결과 목록에 추가된 항목들을 제거합니다
            removeAllChildNods(listEl);

            // 지도에 표시되고 있는 마커를 제거합니다
            removeMarker();

            for (let i = 0; i < places.length; i++) {
                // 마커를 생성하고 지도에 표시합니다
                let placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                    marker = addMarker(
                        placePosition,
                        i,
                        places[i].road_address_name
                            ? places[i].road_address_name
                            : places[i].address_name
                    ),
                    itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

                // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                // LatLngBounds 객체에 좌표를 추가합니다
                bounds.extend(placePosition);

                (function (marker, title) {
                    const btnAddMap = itemEl.querySelector('.btn-add-map');

                    kakao.maps.event.addListener(marker, 'mouseover', function () {
                        displayInfowindow(marker, title);
                    });

                    kakao.maps.event.addListener(marker, 'mouseout', function () {
                        infowindow.close();
                    });

                    kakao.maps.event.addListener(marker, 'click', function () {
                    });

                    itemEl.onmouseover = function () {
                        displayInfowindow(marker, title);
                        btnAddMap.classList.remove('hide');
                    };

                    itemEl.onmouseout = function () {
                        infowindow.close();
                        btnAddMap.classList.add('hide');
                    };

                    //버튼에 이벤트 추가.
                    btnAddMap.addEventListener('click', function (e) {
                        let lat = marker.getPosition().getLat();
                        let lng = marker.getPosition().getLng();
                        let description = marker.getTitle();

                        let staticMapContainer = document.querySelector('#map-container');
                        //정적마커의 옵션
                        let markerPosition = new kakao.maps.LatLng(lat, lng);
                        let staticMarker = {position: markerPosition, text: title};
                        let staticMapOption = {
                            center: new kakao.maps.LatLng(lat, lng),
                            level: 3,
                            marker: staticMarker
                        };
                        new kakao.maps.StaticMap(staticMapContainer, staticMapOption);

                        let link = staticMapContainer.querySelector('a').href;
                        let imgUrl = staticMapContainer.querySelector('img').src;

                        $modal.modal('hide');
                        $editor.summernote('restoreRange');
                        $editor.summernote(
                            'insertNode',
                            template.staticMap(link, imgUrl, description)
                        );
                        staticMapContainer.innerHTML = '';
                    });
                })(marker, places[i].place_name);
                fragment.appendChild(itemEl);
            }
            // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
            listEl.appendChild(fragment);

            // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
            map.setBounds(bounds);
        }

        // 검색결과 항목을 Element로 반환하는 함수입니다
        function getListItem(index, places) {
            let el = document.createElement('li'),
                itemStr = `
                    <div class="box-place">
                      <h6 class="place-name">
                      ${places.place_name}
                      </h6>
                   `;
            if (places.road_address_name) {
                itemStr += `
                    <span class="place-addr">
                    ${places.road_address_name}
                    </span>
                   `;
            } else {
                itemStr += `
                    <span class="place-addr">
                    ${places.address_name}
                    </span>
                   `;
            }
            itemStr += `<button class="btn-add-map hide">추가</button></div>`;

            el.innerHTML = itemStr;
            el.classList.add('list-group-item');

            return el;
        }

        // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
        function addMarker(position, idx, title) {
            let marker = new kakao.maps.Marker({
                position: position // 마커의 위치
            });
            marker.setTitle(title);
            marker.setMap(map); // 지도 위에 마커를 표출합니다
            markers.push(marker); // 배열에 생성된 마커를 추가합니다
            return marker;
        }

        // 지도 위에 표시되고 있는 마커를 모두 제거합니다
        function removeMarker() {
            for (var i = 0; i < markers.length; i++) {
                markers[i].setMap(null);
            }
            markers = [];
        }

        // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
        function displayPagination(pagination) {
            var paginationEl = document.getElementById('pagination'),
                fragment = document.createDocumentFragment(),
                i;

            // 기존에 추가된 페이지번호를 삭제합니다
            while (paginationEl.hasChildNodes()) {
                paginationEl.removeChild(paginationEl.lastChild);
            }

            for (i = 1; i <= pagination.last; i++) {
                var li = document.createElement('li');
                li.className = 'page-item';

                var el = document.createElement('a');
                el.className = 'page-link';
                el.href = '#';
                el.innerHTML = i;

                if (i === pagination.current) {
                    li.classList.add('active');
                } else {
                    el.onclick = (function (i) {
                        return function () {
                            pagination.gotoPage(i);
                        };
                    })(i);
                }
                li.appendChild(el);
                fragment.appendChild(li);
            }
            paginationEl.appendChild(fragment);
        }

        // 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
        // 인포윈도우에 장소명을 표시합니다
        function displayInfowindow(marker, title) {
            let content = '<div style="padding:5px;z-index:1;">' + title + '</div>';
            infowindow.setContent(content);
            infowindow.open(map, marker);
        }

        // 검색결과 목록의 자식 Element를 제거하는 함수입니다
        function removeAllChildNods(el) {
            while (el.hasChildNodes()) {
                el.removeChild(el.lastChild);
            }
        }
    }

    //구글맵 초기화 담당
    function initGoogleMap() {
        //마커를 보관하는 변수
        let markers = [];
        let infowindows = [];
        let infowindow;
        let searchBox;

        let map = new google.maps.Map(
            document.getElementById('map'),
            editor.G_MAP
        );

        // input태그를 잡아와서 search box로 만드는 부분.
        let input = document.getElementById('keyword');
        searchBox = new google.maps.places.SearchBox(input);

        // map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

        // Bias the SearchBox results towards current map's viewport.
        map.addListener('bounds_changed', function () {
            searchBox.setBounds(map.getBounds());
        });

        //검색결과 중 하나 또는 그 이상을 선택 할 경우 아래의 이벤트를 실행.
        searchBox.addListener('places_changed', function () {
            let places = searchBox.getPlaces();
            if (places.length == 0) {
                return;
            }

            //기존 마커를 모두 날림.
            markers.forEach(function (marker) {
                marker.setMap(null);
            });
            markers = [];

            var bounds = new google.maps.LatLngBounds();

            places.forEach(function (place) {
                if (!place.geometry) {
                    return;
                }

                //마커 생성
                let marker = new google.maps.Marker({
                    map: map,
                    title: place.name,
                    position: place.geometry.location
                });

                //마커 이벤트
                marker.addListener('click', function () {
                    //기존 infowindow를 모두 닫음
                    infowindows.forEach((info) => {
                        info.close();
                    });
                    //infowindow를 빈 배열로 초기화.
                    infowindows = [];

                    infowindow = new google.maps.InfoWindow({
                        content: template.infoWindow(place.name, place.formatted_address)
                    });

                    google.maps.event.addListener(infowindow, 'domready', function () {
                        document
                            .querySelector('#btn-add-map')
                            .addEventListener('click', function (e) {
                                let lat = place.geometry.location.lat();
                                let lng = place.geometry.location.lng();
                                let width = 750;
                                let height = 350;
                                let link = `https://www.google.com/maps/search/?api=1&query=${lat},${lng}`;
                                let imgUrl = `https://maps.googleapis.com/maps/api/staticmap?center=${lat},${lng}&zoom=12&size=${width}x${height}&markers=color:red%7C${lat},${lng}&key=${editor.G_KEY}`;

                                $modal.modal('hide');
                                $editor.summernote('restoreRange');
                                $editor.summernote(
                                    'insertNode',
                                    template.staticMap(link, imgUrl, place.formatted_address)
                                );
                            });
                    });
                    infowindow.open(map, marker);
                    infowindows.push(infowindow);
                });

                markers.push(marker);

                if (place.geometry.viewport) {
                    // Only geocodes have viewport.
                    bounds.union(place.geometry.viewport);
                } else {
                    bounds.extend(place.geometry.location);
                }
            });
            map.fitBounds(bounds);
        });
    }

    function getEls(parent, ...targets) {
        let els = [];
        targets.forEach((target) => els.push(parent.querySelector(target)));
        return els;
    }

    function setModal($modal, titleContent, bodyContent) {
        const modal = $modal[0];
        const [title, body] = getEls(modal, '.modal-title', '.modal-body');
        title.innerHTML = titleContent;
        body.innerHTML = bodyContent;
    }


    //ajax호출 모음

    function ajaxGetEssay(rno) {
        return $.ajax({
            type: 'GET',
            contentType: 'application/json',
            url: setting.url + '/api/essay/' + rno
        });
    }

    function ajaxGetEssayListTmp(seq, fixed, order) {
        return $.ajax({
            type: 'GET',
            contentType: 'application/json',
            data: {seq, fixed, order},
            url: setting.url + '/api/essay'
        });
    }

    function ajaxCreate(data) {
        return $.ajax({
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({data}),
            dataType: 'json',
            url: setting.url + '/api/essay',
            beforeSend: setRequestHeader
        });
    }

    function ajaxImageUpload(rno, formData) {
        return $.ajax({
            type: 'POST',
            contentType: false,
            processData: false,
            dataType: 'text',
            data: formData,
            url: setting.url + '/api/essay/' + rno + '/image',
            beforeSend: setRequestHeader
        });
    }

    function ajaxUpdate(data) {
        return $.ajax({
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({data}),
            dataType: 'json',
            url: setting.url + '/api/essay',
            beforeSend: setRequestHeader
        });
    }

    function ajaxDelete(rno) {
        return $.ajax({
            type: 'DELETE',
            contentType: 'application/json',
            dataType: 'json',
            url: setting.url + '/api/essay/' + rno,
            beforeSend: setRequestHeader
        });
    }
});
