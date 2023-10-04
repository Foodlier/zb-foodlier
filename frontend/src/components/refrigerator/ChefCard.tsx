import React from 'react'
import * as S from '../../styles/refrigerator/ChefCard.styled'
import useIcon from '../../hooks/useIcon'

interface RequestedInterface {
  chefId: number
  profileUrl: string
  nickName: string
  introduce: string
  starAvg: number
  distance: number
  reviewCount: number
  recipeCount: number
}

interface ChefType {
  el: RequestedInterface
  isRequest: string
}

const ChefCard: React.FC<ChefType> = ({ el, isRequest }) => {
  const { IcStar } = useIcon()
  return (
    <S.Card>
      <S.CardInfo className="card-info">
        <img src="" alt="프로필 사진" className="mainImg" />
        <S.ChefInfo className="chef-info">
          <S.ChefTopInfo className="top-info">
            <span className="nickName">{el.nickName}</span>
            <IcStar size={1.8} />
            <span className="rating">{`${el.starAvg}(${el.reviewCount})`}</span>
          </S.ChefTopInfo>
          <S.ChefBottomInfo className="bottom-info">
            {el.introduce}
          </S.ChefBottomInfo>
        </S.ChefInfo>
      </S.CardInfo>
      <S.ElseInfo>
        <span>{el.distance}m</span>
        {isRequest === 'false' ? (
          <S.RequestButton type="button">요청하기</S.RequestButton>
        ) : (
          <S.RequestedButton type="button">요청됨</S.RequestedButton>
        )}
      </S.ElseInfo>
    </S.Card>
  )
}

export default ChefCard
