import { useEffect, useState } from 'react'
import Header from '../../components/Header'
import BottomNavigation from '../../components/BottomNavigation'
import * as S from '../../styles/user/MyProfilePage.styled'
import useIcon from '../../hooks/useIcon'
import { palette } from '../../constants/Styles'

interface Review {
  nickName: string
  imgUrl: string
  rating: number
  content: string
}

interface Cooker {
  exp: number
  getReview: Review[]
}

interface UserInfoExam {
  nickName: string
  likeCount: number
  imgUrl: string
  isCooker: boolean
  Cooker?: Cooker
}

const initialUser: UserInfoExam = {
  nickName: '',
  likeCount: 0,
  imgUrl: '',
  isCooker: false,
  Cooker: {
    exp: 0,
    getReview: [],
  },
}

const MyProfilePage = () => {
  const { IcFavoriteFill, ChefHat, InitialChefImg, InitialUserImg } = useIcon()
  const [user, setUser] = useState<UserInfoExam>(initialUser)

  useEffect(() => {
    const USER_INFO_EXAM = {
      nickName: '나는 요리사',
      likeCount: 1000,
      imgUrl: '',
      isCooker: true,
      Cooker: {
        exp: 18,
        getReview: [
          {
            nickName: '나는 요청자1',
            imgUrl: '',
            rating: 4,
            content: '요리사님이 친절하시고 요리도 맛도리있어요!',
          },
          {
            nickName: '나는 요청자2',
            imgUrl: '',
            rating: 3,
            content: '요리사님이 친절하시고 요리도 맛도리있어요!',
          },
          {
            nickName: '나는 요청자3',
            imgUrl: '',
            rating: 3,
            content: '요리사님이 친절하시고 요리도 맛도리있어요!',
          },
        ],
      },
    }
    setUser(USER_INFO_EXAM)
  }, [])

  // 요리사님이 받은 최근 후기는 우선적으로 2개만 표시
  const previewList = user.Cooker?.getReview.slice(0, 2)

  return (
    <>
      <Header />
      <S.MyProfileContainer>
        <S.NormalInfoContainer>
          <S.UserContainer>
            <S.UserImgContainer>
              {user.imgUrl && (
                <S.UserImg src={user.imgUrl} alt="프로필 이미지" />
              )}
              {!user.imgUrl && user.isCooker && <InitialChefImg size={6} />}
              {!user.imgUrl && !user.isCooker && <InitialUserImg size={6} />}
              {user.isCooker && (
                <S.UserIsCooker>
                  <ChefHat size={1.2} />
                  요리사
                </S.UserIsCooker>
              )}
            </S.UserImgContainer>
            <S.UserNickname>{user.nickName}</S.UserNickname>
          </S.UserContainer>
          <S.LikeDiv>
            <IcFavoriteFill size={2.5} color={palette.main} />
            {user.likeCount}
          </S.LikeDiv>
        </S.NormalInfoContainer>
        {user.isCooker && (
          <>
            <S.MyGradeDiv>
              <S.MyGradeP>{user.nickName}님의 등급</S.MyGradeP>
              <S.Bar>
                {user.Cooker?.exp && (
                  <S.ExpBar $cookerExp={(user.Cooker.exp / 50) * 100} />
                )}
              </S.Bar>
              <S.GradeList>
                <span>B</span>
                <span>S</span>
                <span>G</span>
                <span>P</span>
              </S.GradeList>
            </S.MyGradeDiv>
            <div>
              <S.ReviewIntro>
                <S.ReviewTitle>나는 요리사님이 받은 최근 후기</S.ReviewTitle>
                <S.ReviewMoreBtn type="button">+</S.ReviewMoreBtn>
              </S.ReviewIntro>
              <S.ReviewCardList>
                {previewList?.map(el => (
                  <S.ReviewCard key={el.nickName}>
                    <S.ReviewTopInfo>
                      <S.ReviewAuthor>
                        <img src={el.imgUrl} alt="프로필 이미지" />
                        <span>{el.nickName}</span>
                      </S.ReviewAuthor>
                      <div>{el.rating}점</div>
                    </S.ReviewTopInfo>
                    <S.ReviewContent>{el.content}</S.ReviewContent>
                  </S.ReviewCard>
                ))}
              </S.ReviewCardList>
            </div>
          </>
        )}
      </S.MyProfileContainer>
      <BottomNavigation />
    </>
  )
}

export default MyProfilePage
