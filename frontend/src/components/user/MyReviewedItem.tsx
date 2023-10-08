import React, { useState, useEffect } from 'react'
import * as S from '../../styles/user/MyReviewedItem.styled'
import StarRating from '../StarRating'

interface ReviewdItem {
  recipeid: number
  nickname: string
  star: number
  content: string
  cookUrl: string
  createdAt: string
}

interface MyRecipeItemProps {
  EA: number
}

const MyReviewedItem: React.FC<MyRecipeItemProps> = ({ EA }) => {
  const [reviewedItem, setReviewedItem] = useState<ReviewdItem[]>([])
  useEffect(() => {
    const REVIEWED_LIST_EXAM = [
      {
        recipeid: 1,
        nickname: '나는 사용자 1',
        star: 4,
        content: '이 집 잘하네요1',
        cookUrl: '',
        createdAt: '2023-09-28',
      },
      {
        recipeid: 2,
        nickname: '나는 사용자 2',
        star: 5,
        content: '이 집 잘하네요2',
        cookUrl: '',
        createdAt: '2023-09-28',
      },
      {
        recipeid: 3,
        nickname: '나는 사용자 3',
        star: 3,
        content: '이 집 잘하네요3',
        cookUrl: '',
        createdAt: '2023-09-28',
      },
      {
        recipeid: 4,
        nickname: '나는 사용자 4',
        star: 4,
        content: '이 집 잘하네요4',
        cookUrl: '',
        createdAt: '2023-09-28',
      },
      {
        recipeid: 5,
        nickname: '나는 사용자 5',
        star: 4,
        content: '이 집 잘하네요5',
        cookUrl: '',
        createdAt: '2023-09-28',
      },
    ]
    setReviewedItem(REVIEWED_LIST_EXAM)
  }, [])

  const showedCardList = reviewedItem.slice(0, EA)

  return showedCardList.length > 0 ? (
    showedCardList.map(el => (
      <S.ReviewedCard key={el.recipeid}>
        <S.ReviewedImg src="" alt="" />
        <S.ReviewedInfoContainer>
          <S.ReviewedTopInfo>
            <S.ReviewedUserInfo>
              <S.ReviewedUserImg src="" alt="" />
              <S.ReviewedUserName>{el.nickname}</S.ReviewedUserName>
            </S.ReviewedUserInfo>
            <StarRating size={1.6} rating={el.star} />
          </S.ReviewedTopInfo>
          <div>
            <S.ReviewedContent>{el.content}</S.ReviewedContent>
            <S.ReviewedAt>{el.createdAt}</S.ReviewedAt>
          </div>
        </S.ReviewedInfoContainer>
      </S.ReviewedCard>
    ))
  ) : (
    <span>받은 후기가 없습니다.</span>
  )
}

export default MyReviewedItem
