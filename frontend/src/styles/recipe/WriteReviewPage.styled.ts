import styled from 'styled-components'
import { palette, typography, weight } from '../../constants/Styles'

export const ReviewWriteContainer = styled.div`
  width: 100%;
  padding: 5rem 2%;
`

export const ReviewWriteTit = styled.h1`
  width: 100%;
  font-size: 2.5rem;
  padding-bottom: 1rem;
  margin-bottom: 3rem;
  border-bottom: 0.2rem solid ${palette.divider};
`

export const ReviewWriteForm = styled.form`
  width: 100%;
`
export const MoreButtonBox = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 10rem;
  margin-bottom: 15rem;
`
export const ReviewWriteImage = styled.section`
  width: 100%;
`

export const ReviewWriteWrap = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-bottom: 3rem;
`

export const ReviewWriteTxt = styled.h2`
  font-size: ${typography.mobile.subTitle};
  font-weight: ${weight.subTitle};
  margin-bottom: 1.5rem;
`

export const ReviewWriteLabel = styled.label`
  width: 100%;
`

export const ReviewFileLabel = styled.label`
  width: 10rem;
  height: 10rem;
  background: ${palette.divider};
  border-radius: 1rem;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;

  &:hover {
    opacity: 0.7;
  }
`

export const ReviewWriteInput = styled.input`
  display: none;
`

export const ReviewWriteTextarea = styled.textarea`
  border: 0.1rem solid ${palette.divider};
  width: 100%;
  padding: 1rem;
  border-radius: 1rem;
  resize: none;

  &:focus {
    outline: none;
  }
`

export const ReviewWriteStar = styled.div`
  display: inline-flex;
  cursor: pointer;
`

export const ReviewStar = styled.span`
  width: 100%;
`

export const ReviewImage = styled.img`
  width: 10rem;
  height: 10rem;
  background: #000;
  border-radius: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
`
