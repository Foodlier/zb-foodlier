import React, { useState, useEffect } from 'react'
import axiosInstance from '../../utils/FetchCall'
import * as S from '../../styles/user/UserChefInfo.styled'
import StarRating from '../StarRating'
import useIcon from '../../hooks/useIcon'

interface ChefInfo {
  nickName: string
  chefMemberId: number
  EA: number
}

interface ChefGrade {
  grade: string
  exp: number
}

interface ChefReview {
  content: string
  nickname: string
  profileUrl: string
  star: number
}

const UserChefInfo: React.FC<ChefInfo> = ({ EA, nickName, chefMemberId }) => {
  const [chefGrade, setChefGrade] = useState<ChefGrade>({
    grade: 'BRONZE',
    exp: 0,
  })
  const [isGradeLoading, setIsGradeLoading] = useState(false)
  const [chefReview, setChefReview] = useState<ChefReview[]>([])
  const [isReviewLoading, setIsReviewLoading] = useState(false)
  const { InitialUserImg } = useIcon()

  // 쉐프 리뷰 가져오기
  const getChefReview = async () => {
    try {
      const chefReviewRes = await axiosInstance.get(
        `/profile/public/chefreview/${0}/${EA}/${chefMemberId}`
      )
      // console.log('쉐프 리뷰', chefReviewRes.data.content)
      setChefReview(chefReviewRes.data.content)
      setIsReviewLoading(true)
    } catch (error) {
      console.log(error)
    }
  }

  //  쉐프 등급 가져오기
  const getChefGrade = async () => {
    try {
      const chefGradeRes = await axiosInstance.get(
        `/profile/public/chef/${chefMemberId}`
      )
      // console.log('쉐프등급', chefGradeRes.data)
      setChefGrade(chefGradeRes.data)
      setIsGradeLoading(true)
    } catch (error) {
      console.log(error)
    }
  }

  useEffect(() => {
    getChefReview()
    getChefGrade()
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  return (
    <>
      <S.MyGradeDiv>
        <S.MyGradeP>{nickName}님의 등급</S.MyGradeP>
        {isGradeLoading ? (
          <>
            <S.Bar>
              <S.ExpBar $cookerExp={(chefGrade.exp / 50) * 100} />
            </S.Bar>
            <S.GradeList>
              <S.Grade>브론즈</S.Grade>
              <S.Grade>실버</S.Grade>
              <S.Grade>골드</S.Grade>
              <S.Grade>플래티넘</S.Grade>
            </S.GradeList>
          </>
        ) : (
          <S.EtcComment>Loading...</S.EtcComment>
        )}
      </S.MyGradeDiv>

      <S.ReviewContainer>
        <S.ReviewIntro>
          <S.ReviewTitle>{nickName}님의 받은 최근 후기</S.ReviewTitle>
          <S.ReviewMoreBtn type="button">+</S.ReviewMoreBtn>
        </S.ReviewIntro>

        {isReviewLoading && (
          <S.ReviewCardList>
            {chefReview.length > 0 &&
              chefReview.map(el => (
                <S.ReviewCard key={el.nickname}>
                  <S.ReviewTopInfo>
                    <S.ReviewAuthor>
                      {el.profileUrl ? (
                        <S.ReviewImg src={el.profileUrl} alt="프로필 이미지" />
                      ) : (
                        <InitialUserImg size={3} />
                      )}
                      <S.ReviewNickName>{el.nickname}</S.ReviewNickName>
                    </S.ReviewAuthor>
                    <StarRating rating={el.star} size={1.6} />
                  </S.ReviewTopInfo>
                  <S.ReviewContent>{el.content}</S.ReviewContent>
                </S.ReviewCard>
              ))}
          </S.ReviewCardList>
        )}
        {isReviewLoading && chefReview.length === 0 && (
          <S.NoReviewCard>받은 후기가 없습니다.</S.NoReviewCard>
        )}
        {!isReviewLoading && <S.EtcComment>Loading...</S.EtcComment>}
      </S.ReviewContainer>
    </>
  )
}

export default UserChefInfo
