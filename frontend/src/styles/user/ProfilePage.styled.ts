import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const MyProfileContainer = styled.div`
  padding: 5%;

  ${breakpoints.large} {
    padding: 2%;
  }
`

export const NormalInfoContainer = styled.div`
  display: flex;
  gap: 30px;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 60px;
  ${breakpoints.large} {
    justify-content: start;
  }
`

export const UserContainer = styled.div`
  display: flex;
  gap: 1.5rem;
  align-items: center;
`

export const UserImgContainer = styled.div`
  position: relative;
`

export const UserImg = styled.img`
  border-radius: 100%;
  width: 60px;
  height: 60px;
  background-color: yellowgreen;
  object-fit: cover;
`

export const UserIsCooker = styled.div`
  position: absolute;
  top: 70px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 4px;
  justify-content: center;
  align-items: center;
  width: 70px;
  height: 20px;
  border-radius: 10px;
  font-size: 1.2rem;
  line-height: 1.2rem;
  color: ${palette.white};
  background-color: ${palette.main};
`
export const UserNickname = styled.span`
  font-weight: bold;
  font-size: 2rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const LikeDiv = styled.div`
  display: flex;
  align-items: center;
  gap: 10px;
`

export const MyUploadContainer = styled.div`
  margin-bottom: 30px;
`

export const MyUploadIntro = styled.div`
  display: flex;
  justify-content: space-between;
`
export const MyUploadTitle = styled.span`
  font-weight: bold;
  font-size: 1.8rem;
  margin-bottom: 1rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const UploadMoreBtn = styled.button`
  font-size: 2rem;
  padding: 0 10px;
`

export const MyRecipeList = styled.div`
  display: grid;
  justify-items: center;
  row-gap: 1rem;
  grid-template-columns: 1fr 1fr;
  ${breakpoints.large} {
    row-gap: 0;
    grid-template-columns: 1fr 1fr 1fr 1fr;
  }
`

export const MyReviewedContainer = styled.div`
  margin-bottom: 30px;
`

export const MyReviewedInto = styled.div`
  display: flex;
  justify-content: space-between;
`

export const MyReviewedTitle = styled.span`
  font-weight: bold;
  font-size: 1.8rem;
  margin-bottom: 1rem;
  display: -webkit-box;
  overflow: hidden;
  word-break: break-all;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 1;
`

export const ReviewedMoreBtn = styled.button`
  font-size: 2rem;
  padding: 0 10px;
`

export const ReviewedList = styled.div`
  display: flex;
  flex-direction: column;
  gap: 10px;
`

export const SpacingDiv = styled.div`
  height: 93px;
  ${breakpoints.large} {
    display: none;
  }
`
