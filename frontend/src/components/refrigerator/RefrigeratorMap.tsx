import React from 'react'
import { Map, MapMarker } from 'react-kakao-maps-sdk'
import { MarkerItem } from '../../constants/Interfaces'
import * as S from '../../styles/refrigerator/RefrigeratorMap.styled'

const RefrigeratorMap = ({ markerList }: { markerList: MarkerItem[] }) => {
  const localProfile = localStorage.getItem('PROFILE')
  const profile = localProfile ? JSON.parse(localProfile) : {}

  return (
    <S.Container>
      <Map
        center={{
          lat: profile?.address.lat || 0,
          lng: profile?.address.lnt || 0,
        }}
        style={{ width: '100%', height: '100%' }}
        level={5}
        // zoomable={false}
      >
        {markerList.map(markerItem => (
          <React.Fragment key={markerItem.chefId}>
            <MapMarker
              position={{ lat: markerItem.lat, lng: markerItem.lnt }}
              image={{
                src: 'https://github.com/Foodlier/zb-foodlier/assets/137516352/b8258298-d800-4a84-bc3e-529c0868aaeb',
                size: { width: 50, height: 50 },
                options: {
                  offset: {
                    x: 25,
                    y: 25,
                  },
                },
              }}
            />
            {/* <CustomOverlayMap
              position={{ lat: markerItem.lat, lng: markerItem.lnt }}
              yAnchor={1.2}
            >
              <S.Marker>
                <S.FlexWrap>
                  <S.MarkerTitle>{markerItem.nickname}</S.MarkerTitle>
                  <IcStar size={2} />
                  <S.MarkerRate>{`${markerItem.starAvg}(${markerItem.reviewCount})`}</S.MarkerRate>
                </S.FlexWrap>
                <S.MarkerSubTitle>{markerItem.introduce}</S.MarkerSubTitle>
                <S.MarkerButton>요청하기</S.MarkerButton>
              </S.Marker>
            </CustomOverlayMap> */}
          </React.Fragment>
        ))}
      </Map>
    </S.Container>
  )
}

export default RefrigeratorMap
